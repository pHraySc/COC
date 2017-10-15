function select_fix_ie(el) {
	var isOpen = $(el).data("isOpen");
	if (isOpen != null) {
		return;
	}

	$(el).mousedown(function() {
				var isOpen = $(this).data("isOpen");
				if (isOpen) {
					return;
				}
				$(this).data("isOpen", true);

				var clone_selection = $(this).data("clone");
				if (clone_selection == null) {
					clone_selection = $("<span></span>").get(0);
					$(clone_selection).insertAfter(this);
					$(clone_selection).css("display", "block");
					$(clone_selection).css("width", $(this).css("width"));
					$(clone_selection).css("height", "20px");
					$(clone_selection).css("position", $(this).css("position"));
					$(clone_selection).css("z-index", $(this).css("z-index"));
					$(this).data("clone", clone_selection);
				}

				$(clone_selection).show();
				$(clone_selection).css("visibility", "hidden");

				var position = $(this).position();
				var oldWidth = $(this).width();
				$(this).css("width", "auto");
				var newWidth = $(this).width();
				if (oldWidth >= newWidth) {
					$(this).css("width", $(clone_selection).css("width"));
				}
				$(this).css("top", position.top + "px");
				$(this).css("left", position.left + "px");
				$(this).css("position", "absolute");
				$(this).css("z-index", "1000");
			}).blur(function() {
				select_fix_ie_close(this);
			}).change(function() {
				select_fix_ie_close(this);
			})

	$(el).data("isOpen", false);
}

function select_fix_ie_close(el) {
	var isOpen = $(el).data("isOpen");
	if (isOpen) {
		var clone_selection = $(el).data("clone");
		$(clone_selection).css("display", "none");
		$(el).css("position", "");
		$(el).css("width", $(clone_selection).css("width"));
		$(el).css("top", "");
		$(el).css("left", "");
		$(el).css("z-index", $(clone_selection).css("z-index"));
		$(el).data("isOpen", false);
	}
}