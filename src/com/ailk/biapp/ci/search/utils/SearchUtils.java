package com.ailk.biapp.ci.search.utils;

import com.ailk.biapp.ci.search.model.CustomGroupModelForSearch;
import com.ailk.biapp.ci.search.model.LabelModelForSearch;
import com.asiainfo.biframe.utils.config.Configure;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class SearchUtils {
    private static Directory dir = null;
    public static final String SEARCH_INDEX_TYPE_LABEL = "L";
    public static final String SEARCH_LABEL_INDEX_RELATIVE_PATH = "LABEL_INDEX";
    public static final String SEARCH_INDEX_TYPE_CUSTOM = "C";

    public SearchUtils() {
    }

    public static Version getLuceneVersion() {
        return Version.LUCENE_46;
    }

    public void createLabelIndex(List<LabelModelForSearch> labelModelList) {
        String indexType = "L";
        Directory dir = this.getDirectory(indexType);
        IndexWriterConfig iwc = new IndexWriterConfig(getLuceneVersion(), getAnalyzer());

        try {
            FSDirectory e = FSDirectory.open(new File(this.getIndexPath(indexType)));
            IndexWriter ramWriter = new IndexWriter(dir, iwc);
            IndexWriter fsWriter = new IndexWriter(e, iwc);
            Document document = null;
            if(null != labelModelList && labelModelList.size() > 0) {
                Iterator iterator = labelModelList.iterator();

                while(iterator.hasNext()) {
                    LabelModelForSearch labelModelForSearch = (LabelModelForSearch)iterator.next();
                    document = new Document();
                    document.add(new IntField("labelId", labelModelForSearch.getLabelId().intValue(), Store.YES));
                    StringField labelName = new StringField("labelName", labelModelForSearch.getLabelName(), Store.YES);
                    labelName.setBoost(100.0F);
                    document.add(labelName);
                    ramWriter.addDocument(document);
                }

                fsWriter.addIndexes(new Directory[]{dir});
                ramWriter.close();
                fsWriter.close();
            }
        } catch (IOException var12) {
            var12.printStackTrace();
        }

    }

    public void createCustomIndex(List<CustomGroupModelForSearch> customModelList) {
        String indexType = "C";
        Directory dir = this.getDirectory(indexType);
        IndexWriterConfig iwc = new IndexWriterConfig(getLuceneVersion(), getAnalyzer());

        try {
            FSDirectory e = FSDirectory.open(new File(this.getIndexPath(indexType)));
            IndexWriter ramWriter = new IndexWriter(dir, iwc);
            IndexWriter fsWriter = new IndexWriter(e, iwc);
            Document document = null;
            if(null != customModelList && customModelList.size() > 0) {
                Iterator iterator = customModelList.iterator();

                while(iterator.hasNext()) {
                    CustomGroupModelForSearch customModelForSearch = (CustomGroupModelForSearch)iterator.next();
                    document = new Document();
                    document.add(new StringField("customGroupId", customModelForSearch.getCustomGroupId(), Store.YES));
                    StringField customGroupName = new StringField("customGroupName", customModelForSearch.getCustomGroupName(), Store.YES);
                    customGroupName.setBoost(100.0F);
                    document.add(customGroupName);
                    ramWriter.addDocument(document);
                }

                fsWriter.addIndexes(new Directory[]{dir});
                ramWriter.close();
                fsWriter.close();
            }
        } catch (IOException var12) {
            var12.printStackTrace();
        }

    }

    public String getIndexPath(String type) {
        String path = "";
        if("L".equalsIgnoreCase(type)) {
            try {
                path = Configure.getInstance().getProperty("LABEL_INDEX_PATH");
                File e = new File(path);
                if(!e.exists()) {
                    e.mkdirs();
                }

                path = e.getAbsolutePath();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        } else if("C".equalsIgnoreCase(type)) {
            path = Configure.getInstance().getProperty("CUSTOM_GROUP_INDEX_PATH");
        }

        return path;
    }

    public static Analyzer getAnalyzer() {
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(getLuceneVersion());
        return analyzer;
    }

    public Directory getDirectory(String type) {
        if(null == dir) {
            try {
                FSDirectory e = FSDirectory.open(new File(this.getIndexPath(type)));
                dir = new RAMDirectory(e, IOContext.READ);
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

        return dir;
    }

    public List<LabelModelForSearch> searchLabel(String[] fields, String[] fieldsValue, Occur[] flags, int first, int max) {
        ArrayList labelModelList = new ArrayList();
        BooleanQuery query = new BooleanQuery();

        for(int reader = 0; reader < fields.length; ++reader) {
            query.add(new TermQuery(new Term(fields[reader], fieldsValue[reader])), flags[reader]);
        }

        try {
            DirectoryReader var18 = DirectoryReader.open(this.getDirectory("L"));
            IndexSearcher e = new IndexSearcher(var18);
            TopDocs topDocs = e.search(query, 10000);
            int end = Math.min(first + max, topDocs.totalHits);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            for(int i = first; i < end; ++i) {
                int doc = scoreDocs[i].doc;
                Document document = e.doc(doc);
                LabelModelForSearch labelModel = new LabelModelForSearch();
                labelModel.setLabelId(Integer.valueOf(document.get("labelId")));
                labelModel.setLabelName(document.get("labelName"));
                labelModelList.add(labelModel);
            }

            var18.close();
        } catch (IOException var17) {
            var17.printStackTrace();
        }

        return labelModelList;
    }
}
