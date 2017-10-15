package com.ailk.biapp.ci.service.impl;

import com.ailk.biapp.ci.dao.ICiProductInfoHDao;
import com.ailk.biapp.ci.dao.ICiProductInfoJDao;
import com.ailk.biapp.ci.entity.CiProductCategory;
import com.ailk.biapp.ci.entity.CiProductInfo;
import com.ailk.biapp.ci.entity.DimBrand;
import com.ailk.biapp.ci.exception.CIServiceException;
import com.ailk.biapp.ci.model.CiProductInfoTree;
import com.ailk.biapp.ci.model.LabelInfoTreeModel;
import com.ailk.biapp.ci.model.ProductDetailInfo;
import com.ailk.biapp.ci.model.TreeNode;
import com.ailk.biapp.ci.service.ICiProductService;
import com.ailk.biapp.ci.util.DateUtil;
import com.ailk.biapp.ci.util.cache.CacheBase;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CiProductServiceImpl implements ICiProductService {
    private Logger log = Logger.getLogger(CiProductServiceImpl.class);
    @Autowired
    private ICiProductInfoHDao ciProductInfoHDao;
    @Autowired
    private ICiProductInfoJDao ciProductInfoJDao;

    public CiProductServiceImpl() {
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiProductCategory> queryProductFirstCategoryList() throws CIServiceException {
        ArrayList productCategoryList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveProductCategory = e.getObjectList("ALL_PRODUCT_CATEGORY_MAP");
            Iterator i$ = allEffectiveProductCategory.iterator();

            while(i$.hasNext()) {
                CiProductCategory ciProductCategory = (CiProductCategory)i$.next();
                if(ciProductCategory.getParentId().compareTo(Integer.valueOf(-1)) == 0) {
                    productCategoryList.add(ciProductCategory);
                }
            }

            return productCategoryList;
        } catch (Exception var6) {
            this.log.error("查询第一级有效标签失败", var6);
            throw new CIServiceException("查询第一级有效标签失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiProductCategory> queryProductSecondCategory(Integer categoryId) throws CIServiceException {
        ArrayList productCategoryList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveProductCategory = e.getObjectList("ALL_PRODUCT_CATEGORY_MAP");
            Iterator i$ = allEffectiveProductCategory.iterator();

            while(i$.hasNext()) {
                CiProductCategory ciProductCategory = (CiProductCategory)i$.next();
                if(ciProductCategory.getParentId().compareTo(categoryId) == 0) {
                    productCategoryList.add(ciProductCategory);
                }
            }

            return productCategoryList;
        } catch (Exception var7) {
            this.log.error("根据标签Id查询标签失败", var7);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public int queryProductSecondCategoryCount(Integer categoryId) throws CIServiceException {
        boolean count = false;

        try {
            List e = this.queryProductSecondCategory(categoryId);
            int count1 = e.size();
            return count1;
        } catch (Exception var4) {
            this.log.error("查询总记录数失败", var4);
            throw new CIServiceException("查询总记录数失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiProductInfo> queryFirstLevelProductByCategoryId(Integer categoryId) throws CIServiceException {
        ArrayList productList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveProduct = e.getObjectList("ALL_EFFECTIVE_PRODUCT_MAP");
            Iterator i$ = allEffectiveProduct.iterator();

            while(i$.hasNext()) {
                CiProductInfo ciProductInfo = (CiProductInfo)i$.next();
                if(ciProductInfo.getCategoryId().compareTo(categoryId) == 0 && ciProductInfo.getParentId().compareTo(Integer.valueOf(-1)) == 0) {
                    productList.add(ciProductInfo);
                }
            }

            return productList;
        } catch (Exception var7) {
            this.log.error("根据产品的二级类别id，找到第一级产品的list失败", var7);
            throw new CIServiceException("根据产品的二级类别id，找到第一级产品的list失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List queryProductPage(int currPage, int pageSize, int categoryId) throws CIServiceException {
        ArrayList secondCategoryAndProductsList = new ArrayList();

        try {
            List e = this.queryProductSecondCategory(Integer.valueOf(categoryId));
            int begin = (currPage - 1) * pageSize;
            int end = begin + pageSize;
            if(end > e.size()) {
                end = e.size();
            }

            List currPageList = e.subList(begin, end);

            for(int i = 0; i < currPageList.size(); ++i) {
                int productTreeDepth = 0;
                HashMap secondCategoryAndProducts = new HashMap();
                CiProductCategory CiProductCategory = (CiProductCategory)currPageList.get(i);
                List ciProductInfoList = this.queryFirstLevelProductByCategoryId(CiProductCategory.getCategoryId());
                ArrayList ciProductInfoTreeList = new ArrayList();

                for(int j = 0; j < ciProductInfoList.size(); ++j) {
                    CiProductInfo ciProductInfo = (CiProductInfo)ciProductInfoList.get(j);
                    ciProductInfo.setBrandNames(this.getBrandNamesById(ciProductInfo.getBrandId()));
                    String effectTime = "";
                    String failTime = "";
                    if(ciProductInfo.getEffecTime() != null) {
                        effectTime = (new SimpleDateFormat("yyyy-MM-dd")).format(ciProductInfo.getEffecTime());
                    }

                    if(ciProductInfo.getFailTime() != null) {
                        failTime = (new SimpleDateFormat("yyyy-MM-dd")).format(ciProductInfo.getFailTime());
                    }

                    if(StringUtil.isNotEmpty(effectTime) && StringUtil.isNotEmpty(failTime)) {
                        ciProductInfo.setEffectPeriod(effectTime + " 至  " + failTime);
                    }

                    CiProductInfoTree productTree = this.getProductTree(ciProductInfo);
                    ciProductInfoTreeList.add(productTree);
                    if(productTreeDepth < productTree.depth) {
                        productTreeDepth = productTree.depth;
                    }
                }

                secondCategoryAndProducts.put("category", CiProductCategory);
                secondCategoryAndProducts.put("productTrees", ciProductInfoTreeList);
                secondCategoryAndProducts.put("productTreeDepth", Integer.valueOf(productTreeDepth));
                secondCategoryAndProductsList.add(secondCategoryAndProducts);
            }

            return secondCategoryAndProductsList;
        } catch (Exception var20) {
            this.log.error("查询分页记录失败", var20);
            throw new CIServiceException("查询分页记录失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public Map querySecondCategoryProductPage(int categoryId) throws CIServiceException {
        HashMap secondCategoryAndProducts = new HashMap();

        try {
            int e = 0;
            CacheBase cache = CacheBase.getInstance();
            CiProductCategory ciProductCategory = cache.getProductCategory(categoryId + "");
            List ciProductInfoList = this.queryFirstLevelProductByCategoryId(ciProductCategory.getCategoryId());
            ArrayList ciProductInfoTreeList = new ArrayList();

            for(int j = 0; j < ciProductInfoList.size(); ++j) {
                CiProductInfo ciProductInfo = (CiProductInfo)ciProductInfoList.get(j);
                ciProductInfo.setBrandNames(this.getBrandNamesById(ciProductInfo.getBrandId()));
                String effectTime = (new SimpleDateFormat("yyyy-MM-dd")).format(ciProductInfo.getEffecTime());
                String failTime = (new SimpleDateFormat("yyyy-MM-dd")).format(ciProductInfo.getFailTime());
                ciProductInfo.setEffectPeriod(effectTime + " 至  " + failTime);
                CiProductInfoTree productTree = this.getProductTree(ciProductInfo);
                ciProductInfoTreeList.add(productTree);
                if(e < productTree.depth) {
                    e = productTree.depth;
                }
            }

            secondCategoryAndProducts.put("category", ciProductCategory);
            secondCategoryAndProducts.put("productTrees", ciProductInfoTreeList);
            secondCategoryAndProducts.put("productTreeDepth", Integer.valueOf(e));
            return secondCategoryAndProducts;
        } catch (Exception var13) {
            this.log.error("查询分页记录失败", var13);
            throw new CIServiceException("查询分页记录失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public CiProductInfoTree getProductTree(CiProductInfo ciProductInfo) {
        CiProductInfoTree tree = new CiProductInfoTree();
        tree.setCiProductInfo(ciProductInfo);
        ArrayList childTreeList = new ArrayList();
        Integer labelId = ciProductInfo.getProductId();
        List childrenProducts = this.queryProductChildrenById(labelId);
        if(childrenProducts == null || childrenProducts.isEmpty()) {
            tree.isLeaf = Boolean.valueOf(true);
            tree.depth = 1;
        }

        int childTreeDepth;
        for(childTreeDepth = 0; childTreeDepth < childrenProducts.size(); ++childTreeDepth) {
            CiProductInfo i$ = (CiProductInfo)childrenProducts.get(childTreeDepth);
            Date childTree = i$.getEffecTime();
            String effectDate = (new SimpleDateFormat("yyyyMM")).format(childTree);
            i$.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 0));
            CiProductInfoTree mytree = this.getProductTree(i$);
            if(mytree != null) {
                childTreeList.add(mytree);
            }
        }

        if(childTreeList != null && !childTreeList.isEmpty()) {
            tree.setCiProductInfoTree(childTreeList);
            childTreeDepth = 0;
            Iterator var11 = childTreeList.iterator();

            while(var11.hasNext()) {
                CiProductInfoTree var12 = (CiProductInfoTree)var11.next();
                if(childTreeDepth < var12.getDepth()) {
                    childTreeDepth = var12.getDepth();
                }
            }

            ++childTreeDepth;
            tree.setDepth(childTreeDepth);
        } else {
            tree.isLeaf = Boolean.valueOf(true);
            tree.depth = 1;
        }

        return tree;
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiProductInfo> queryProductChildrenById(Integer productId) throws CIServiceException {
        ArrayList productList = new ArrayList();

        try {
            CacheBase e = CacheBase.getInstance();
            CopyOnWriteArrayList allEffectiveProduct = e.getObjectList("ALL_EFFECTIVE_PRODUCT_MAP");
            Iterator i$ = allEffectiveProduct.iterator();

            while(i$.hasNext()) {
                CiProductInfo ciProductInfo = (CiProductInfo)i$.next();
                if(ciProductInfo.getParentId().compareTo(Integer.valueOf(productId.intValue())) == 0) {
                    ciProductInfo.setBrandNames(this.getBrandNamesById(ciProductInfo.getBrandId()));
                    String effectTime = (new SimpleDateFormat("yyyy-MM-dd")).format(ciProductInfo.getEffecTime());
                    String failTime = (new SimpleDateFormat("yyyy-MM-dd")).format(ciProductInfo.getFailTime());
                    ciProductInfo.setEffectPeriod(effectTime + " 至  " + failTime);
                    productList.add(ciProductInfo);
                }
            }

            return productList;
        } catch (Exception var9) {
            this.log.error("根据标签Id查询标签失败", var9);
            throw new CIServiceException("根据标签Id查询标签失败");
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<CiProductInfo> queryEffectiveCiProductInfo() throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductInfoHDao.selectEffectiveCiProductInfo();
            return list;
        } catch (Exception var4) {
            String message = "查询所有有效的产品错误";
            this.log.error(message, var4);
            throw new CIServiceException(message, var4);
        }
    }

    @Transactional(
        propagation = Propagation.NOT_SUPPORTED,
        readOnly = true
    )
    public List<ProductDetailInfo> queryEffectiveCiProductInfo(int currentPage, int pageSize, String orderType, String productName, Integer topLabelId, String dataScope) throws CIServiceException {
        List list = null;

        try {
            list = this.ciProductInfoJDao.getEffectiveProduct(currentPage, pageSize, orderType, productName, topLabelId, dataScope);
            Iterator e = list.iterator();

            while(e.hasNext()) {
                ProductDetailInfo message1 = (ProductDetailInfo)e.next();
                Date d = message1.getEffecTime();
                String effectDate = (new SimpleDateFormat("yyyyMM")).format(d);
                message1.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 0));
                message1.setBrandNames(this.getBrandNamesById(message1.getBrandId()));
                message1.setAncestorNames(this.getAncestorNamesByProductInfo(message1));
            }

            return list;
        } catch (Exception var12) {
            String message = "查询所有有效的产品中的一页错误";
            this.log.error(message, var12);
            throw new CIServiceException(message, var12);
        }
    }

    public long queryEffectiveCiProductInfoNum(String productName, Integer topLabelId, String dataScope) throws CIServiceException {
        try {
            long count = this.ciProductInfoJDao.getEffectiveProductNum(productName, topLabelId, dataScope);
            return count;
        } catch (Exception var7) {
            this.log.error("查询首页标签列表失败", var7);
            throw new CIServiceException("查询首页标签列表失败");
        }
    }

    public List<TreeNode> queryProductTree() throws CIServiceException {
        ArrayList treeList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList categoryIds = cache.getKeyList("ALL_PRODUCT_CATEGORY_MAP");
        CopyOnWriteArrayList productIds = cache.getKeyList("ALL_EFFECTIVE_PRODUCT_MAP");

        String message;
        try {
            Iterator e = categoryIds.iterator();

            TreeNode treeNode;
            while(e.hasNext()) {
                message = (String)e.next();
                CiProductCategory product = cache.getProductCategory(message);
                if(-1 != product.getParentId().intValue()) {
                    treeNode = this.newTreeNode((CiProductInfo)null, product, false);
                    treeList.add(treeNode);
                }
            }

            e = productIds.iterator();

            while(e.hasNext()) {
                message = (String)e.next();
                CiProductInfo product1 = cache.getEffectiveProduct(message);
                treeNode = this.newTreeNode(product1, (CiProductCategory)null, true);
                treeList.add(treeNode);
            }

            return treeList;
        } catch (Exception var9) {
            message = "产品树查询错误";
            this.log.error(message, var9);
            throw new CIServiceException(message, var9);
        }
    }

    public List<TreeNode> queryProductTreeByName(String productName) throws CIServiceException {
        ArrayList treeList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        HashSet set = new HashSet();
        CopyOnWriteArrayList productIds = cache.getKeyList("ALL_EFFECTIVE_PRODUCT_MAP");
        Iterator i$ = productIds.iterator();

        while(i$.hasNext()) {
            String pid = (String)i$.next();

            try {
                CiProductInfo e = cache.getEffectiveProduct(pid);
                if(!StringUtil.isEmpty(e.getProductName()) && (!StringUtil.isNotEmpty(e.getProductName()) || e.getProductName().contains(productName))) {
                    set.add(e.getProductId());
                    this.searchParentNode(treeList, e, productName, set);
                    TreeNode message1 = this.newTreeNode(e, (CiProductCategory)null, true);
                    treeList.add(message1);
                }
            } catch (Exception var10) {
                String message = "产品树模糊查询错误";
                this.log.error(message, var10);
                throw new CIServiceException(message, var10);
            }
        }

        return treeList;
    }

    private void searchParentNode(List<TreeNode> treeList, CiProductInfo product, String productName, Set<Integer> set) {
        CacheBase cache = CacheBase.getInstance();
        TreeNode categoryNode;
        if(product.getParentId().intValue() != -1) {
            if(!set.contains(product.getParentId())) {
                CiProductInfo category = cache.getEffectiveProduct(product.getParentId() + "");
                if(!category.getProductName().contains(productName)) {
                    set.add(category.getProductId());
                    categoryNode = this.newTreeNode(category, (CiProductCategory)null, true);
                    treeList.add(categoryNode);
                    this.searchParentNode(treeList, category, productName, set);
                }
            }
        } else if(!set.contains(product.getCategoryId())) {
            CiProductCategory category1 = cache.getProductCategory(product.getCategoryId() + "");
            set.add(category1.getCategoryId());
            categoryNode = this.newTreeNode((CiProductInfo)null, category1, false);
            treeList.add(categoryNode);
        }

    }

    private TreeNode newTreeNode(CiProductInfo product, CiProductCategory category, boolean flag) {
        TreeNode treeNode = new TreeNode();
        if(flag) {
            if(product != null && product.getParentId() != null) {
                ActionContext ctx = ActionContext.getContext();
                HttpServletRequest request = (HttpServletRequest)ctx.get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
                String contextPath = request.getContextPath();
                treeNode.setId(product.getProductId() + "");
                treeNode.setName(product.getProductName());
                if(-1 == product.getParentId().intValue()) {
                    treeNode.setpId(product.getCategoryId() + "");
                } else {
                    treeNode.setpId(product.getParentId() + "");
                }

                treeNode.setIsParent(Boolean.FALSE);
                treeNode.setOpen(Boolean.FALSE);
                treeNode.setTip(product.getProductName());
                String signIconPath = contextPath + Configure.getInstance().getProperty("SIGN_LABEL_FOR_TREE");
                treeNode.setIcon(signIconPath);
                treeNode.setParam(this.newLabelTreeModel(product, category));
                if(product.getIsProduct() != null) {
                    treeNode.setClick(Boolean.valueOf(product.getIsProduct().intValue() != 0));
                } else {
                    treeNode.setClick(Boolean.valueOf(false));
                }
            }
        } else if(category != null && category.getCategoryId() != null) {
            treeNode.setId(category.getCategoryId() + "");
            treeNode.setName(category.getCategoryName());
            treeNode.setpId("0");
            treeNode.setOpen(Boolean.FALSE);
            treeNode.setIsParent(Boolean.TRUE);
            treeNode.setTip(category.getCategoryName());
        }

        return treeNode;
    }

    private LabelInfoTreeModel newLabelTreeModel(CiProductInfo product, CiProductCategory category) {
        LabelInfoTreeModel model = new LabelInfoTreeModel();
        Date d = product.getEffecTime();
        String effectDate = (new SimpleDateFormat("yyyyMM")).format(d);
        model.setEffectDate(DateUtil.getOffsetDateByDate(effectDate, -1, 0));
        if(product.getIsProduct() != null) {
            model.setIsCanDrag(product.getIsProduct());
        } else {
            model.setIsCanDrag(Integer.valueOf(0));
        }

        return model;
    }

    public List<CiProductInfo> queryProductName(String productName, Integer firstCategoryId) throws CIServiceException {
        ArrayList resultList = new ArrayList();
        CacheBase cache = CacheBase.getInstance();
        int count = 0;
        CopyOnWriteArrayList productList;
        if(firstCategoryId != null) {
            productList = cache.getObjectList("ALL_PRODUCT_CATEGORY_MAP");
            CopyOnWriteArrayList i$ = cache.getObjectList("ALL_EFFECTIVE_PRODUCT_MAP");
            Iterator ciProductInfo = productList.iterator();

            while(true) {
                CiProductCategory ciProductCategory;
                do {
                    if(!ciProductInfo.hasNext()) {
                        return resultList;
                    }

                    ciProductCategory = (CiProductCategory)ciProductInfo.next();
                } while(ciProductCategory.getParentId().compareTo(firstCategoryId) != 0);

                Iterator i$1 = i$.iterator();

                while(i$1.hasNext()) {
                    CiProductInfo ciProductInfo1 = (CiProductInfo)i$1.next();
                    if(ciProductInfo1.getCategoryId().equals(ciProductCategory.getCategoryId()) && ciProductInfo1.getProductName().toLowerCase().contains(productName.toLowerCase()) && ciProductInfo1.getIsProduct().intValue() == 1) {
                        resultList.add(ciProductInfo1);
                        ++count;
                        if(count == 10) {
                            return resultList;
                        }
                    }
                }
            }
        } else {
            productList = cache.getObjectList("ALL_EFFECTIVE_PRODUCT_MAP");
            Iterator var12 = productList.iterator();

            while(var12.hasNext()) {
                CiProductInfo var13 = (CiProductInfo)var12.next();
                if(var13.getProductName().toLowerCase().contains(productName.toLowerCase()) && var13.getIsProduct().intValue() == 1) {
                    resultList.add(var13);
                    ++count;
                    if(count == 10) {
                        return resultList;
                    }
                }
            }

            return resultList;
        }
    }

    private String getBrandNamesById(String ids) {
        String[] idsArray = new String[0];
        StringBuffer brandNames = new StringBuffer();
        CacheBase cache = CacheBase.getInstance();
        CopyOnWriteArrayList dimBrand = cache.getObjectList("DIM_BRAND");
        if(StringUtil.isNotEmpty(ids)) {
            idsArray = ids.split(",");
        }

        String[] arr$ = idsArray;
        int len$ = idsArray.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String id = arr$[i$];

            for(int i = 0; i < dimBrand.size(); ++i) {
                if(id.equals(((DimBrand)dimBrand.get(i)).getBrandId().toString())) {
                    brandNames.append(((DimBrand)dimBrand.get(i)).getBrandName().toString() + ",");
                }
            }
        }

        if(brandNames.length() > 0) {
            return brandNames.toString().substring(0, brandNames.toString().length() - 1);
        } else {
            return "";
        }
    }

    private String getAncestorNamesByProductInfo(ProductDetailInfo productDetailInfo) {
        CacheBase cache = CacheBase.getInstance();
        int categoryId = productDetailInfo.getCategoryId().intValue();
        CiProductCategory ciProductCategory = cache.getProductCategory(categoryId + "");
        String categoryName = ciProductCategory.getCategoryName();
        int categoryParentId = ciProductCategory.getParentId().intValue();
        String parentName = "";
        String ancestorNames = "";
        int productParentId = productDetailInfo.getParentId().intValue();

        while(productParentId != -1) {
            CiProductInfo ciProductInfo = cache.getEffectiveProduct(productParentId + "");
            if(ciProductInfo == null) {
                break;
            }

            parentName = ciProductInfo.getProductName();
            productParentId = ciProductInfo.getParentId().intValue();
            if(StringUtil.isNotEmpty(ancestorNames)) {
                ancestorNames = parentName + " >> " + ancestorNames;
            } else {
                ancestorNames = parentName;
            }
        }

        if(StringUtil.isNotEmpty(ancestorNames)) {
            ancestorNames = categoryName + " >> " + ancestorNames;
        } else {
            ancestorNames = categoryName;
        }

        while(categoryParentId != -1) {
            ciProductCategory = cache.getProductCategory(categoryParentId + "");
            if(ciProductCategory == null) {
                break;
            }

            categoryName = ciProductCategory.getCategoryName();
            categoryParentId = ciProductCategory.getParentId().intValue();
            if(StringUtil.isNotEmpty(ancestorNames)) {
                ancestorNames = categoryName + " >> " + ancestorNames;
            } else {
                ancestorNames = categoryName;
            }
        }

        return ancestorNames;
    }
}
