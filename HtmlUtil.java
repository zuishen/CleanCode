package com.zboot.cleancode.chapter03;

public class HtmlUtil {

    public static String testableHtml(PageData pageData, boolean isSuiteSetup) throws Exception {

        boolean isTestPage = pageData.hasAttribute("Test");
        if (isTestPage) {
            WikiPage testPage = pageData.getwikiPage();
            StringBuffer newPageContent = new StringBuffer();
            includeSetupPages(testPage, newPageContent, isSuiteSetup);
            newPageContent.append(pageData.getContent());
            includTearDownPage(testPage, newPageContent, isSuiteSetup);
            pageData.setContent(newPageContent.toString());
        }
        return pageData.getHtml();
    }

    private static void includeSetupPages(WikiPage testPage, StringBuffer newPageContent, boolean isSuiteSetup) {

        WikiPage setup = PageCrawlerImpl.getInheritedPage("SetUp", testPage);
        if (setup != null) {
            WikiPagePath setupPath = testPage.getPageCrawler().getFullPath(setup);
            String setupPathName = PathParser.render(setupPath);
            newPageContent.append("!include -setup.").append(setupPathName).append("\n");
        }

        if (isSuiteSetup) {
            WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_SETUP_NAME, testPage);
            if (suiteSetup != null) {
                WikiPagePath pagePath = suiteSetup.getPageCrawler().getFullpath(suiteSetup);
                String pagePathName = PathParser.render(pagePath);
                newPageContent.append("!include -setup.").append(pagePathName).append("\n");
            }
        }

    }

    private static void includTearDownPage(WikiPage testPage, StringBuffer newPageContent, boolean isSuiteSetup) {

        WikiPage teardown = PageCrawlerImpl.getInheritedPage("TearDown", testPage);
        if (teardown != null) {
            WikiPagePath tearDownPath = testPage.getPageCrawler().getFullPath(teardown);
            String tearDownPathName = PathParser.render(tearDownPath);
            newPageContent.append("\n").append("!include -teardown.").append(tearDownPathName).append("\n");
        }

        if (isSuiteSetup) {
            WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(SuiteResponder.SUITE_TEARDOWN_NAME, testPage);
            if (suiteTeardown != null) {
                WikiPagePath pagePath = suiteTeardown.getPageCrawler().getFullPath(suiteTeardown);
                String pagePathName = PathParser.render(pagePath);
                newPageContent.append("!include -teardown.").append(pagePathName).append("\n");
            }
        }

    }
}
