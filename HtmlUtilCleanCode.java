package com.zboot.cleancode.chapter03;

public class HtmlUtilCleanCode {
    private WikiPage testPage;
    private StringBuffer newPageContent;
    private boolean isSuiteSetup;
    private PageCrawler pageCrawler;
    private PageData pageData;

    public static String reader(PageData pageData) {
        return reader(pageData, false);
    }

    public static String reader(PageData pageData, boolean isSuite) {
        return new HtmlUtilCleanCode(pageData).reader(isSuite);
    }

    private HtmlUtilCleanCode(PageData pageData) {
        this.testPage = pageData.getwikiPage();
        this.pageCrawler = testPage.getPageCrawler();
        newPageContent = new StringBuffer();
    }

    private String reader(boolean isSuite) {
        this.isSuiteSetup = isSuite;
        if (this.isTestPage()) {
            this.includSetupAndTearPages();
        }
        return this.pageData.getHtml();
    }

    private boolean isTestPage() {
        return this.pageData.hasAttribute("test");
    }

    private void includSetupAndTearPages() {
        includeSetupPages();
        includePageContent();
        includeTearPages();
    }

    private void includeSetupPages() {
        if (this.isSuiteSetup) {
            include("SuiteResponder.SUITE_SETUP_NAME", "-setup");
        }
        includeSetupPage();
    }

    private void includeSetupPage() {
        include("SetUp", "-setup");
    }

    private void includePageContent() {
        newPageContent.append(pageData.getContent());
    }

    private void includeTearPages() {
        if (this.isSuiteSetup) {
            include("SuiteResponder.SUITE_SETUP_NAME", "-teardown");
        }
        includeTearPage();
    }

    private void includeTearPage() {
        include("TearDown", "-teardown");
    }

    private void include(String pageName, String args) {
        WikiPage innerPage = PageCrawlerImpl.getInheritedPage(pageName, this.testPage);
        if (innerPage != null) {
            WikiPagePath pagePath = innerPage.getPageCrawler().getFullpath(innerPage);
            String pagePathName = PathParser.render(pagePath);
            newPageContent.append("!include -").append(args).append(".").append(pagePathName).append("\n");
        }
    }

}
