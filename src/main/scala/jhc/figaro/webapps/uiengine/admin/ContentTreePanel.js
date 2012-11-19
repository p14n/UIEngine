var ContentTreePanel = {};
(function($){
	var pathBitOf = function(fullPath){
		var index = fullPath.lastIndexOf("/");
		if(index>-1){
			return fullPath.substring(0,index)
		}
		return "";
	}
	var fileBitOf = function(fullPath){
		var index = fullPath.lastIndexOf("/");
		if(index>-1){
			return fullPath.substring(index+1)
		}
		return fullPath;
	}
	var toggleHandler = function(classToToggle){
		return function() {
			$("."+classToToggle).toggle()
		}
	}
	var createClickableFolderElement = function(path,folderCnt){
		if(path==""){
			path="&lt;root&gt;"
		}

		var heading = $("<div><span class=\"clickablefolder\">"+path+"</span></div>")
		var folderClassName = "folder_"+folderCnt;
		heading.click(toggleHandler(folderClassName));
		$("."+folderClassName).toggle()
		return heading;
	}
	ContentTreePanel.createTree = function(ulClass) {
		var currPath = null
		var folderCount = 0
		$($("."+ulClass+" .contentitem").get().reverse()).each(function() { 
			var pathSpan = $(this).find(".path")
			var pathText = pathSpan.text()
			pathSpan.text(fileBitOf(pathText))
			if(currPath==null){
				currPath = pathBitOf(pathText)
				$(this).addClass("folder_"+folderCount);
			} else if(currPath!=pathBitOf(pathText)){
				var heading = createClickableFolderElement(currPath,folderCount);
				$(this).after(heading)
				currPath = pathBitOf(pathText)
				folderCount++;
				$(this).addClass("folder_"+folderCount);
			} else {
				$(this).addClass("folder_"+folderCount);
			}
		});
		if(currPath!=null){
			var heading = createClickableFolderElement(
				currPath,folderCount);
			$("."+ulClass).prepend(heading);
		}
	}
}(jQuery));