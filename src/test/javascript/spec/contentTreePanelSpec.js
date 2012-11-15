describe('Content Tree Panel', function () {

    beforeEach(function () {
        
    });
    
    it('should verify Jasmine is running', function () {
        expect(1 + 2).toBe(3);
    });
    it('should build the tree', function () {
        ContentTreePanel.createTree('contentlist')

        folders = jQuery('.contentlist').find('.clickablefolder');
        expect(folders.first().text()=='one').toBe(true);
        expect($(folders.get(1)).text()=='two').toBe(true);
        expect($(folders.get(2)).text()=='<root>').toBe(true);
        expect($(folders.get(3)).text()=='one/three').toBe(true);
    });
});
