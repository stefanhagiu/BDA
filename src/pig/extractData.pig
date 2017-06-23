register /C:/cygwin64/pig-0.15.0-src/lib/piggybank.jar;
DEFINE XPath org.apache.pig.piggybank.evaluation.xml.XPath();
DEFINE XPathAll org.apache.pig.piggybank.evaluation.xml.XPathAll();
DEFINE CSVExcelStorage org.apache.pig.piggybank.storage.CSVExcelStorage;


A = load '/pmc_oa_test_data' using org.apache.pig.piggybank.storage.XMLLoader('record') as (x: chararray);

B = FOREACH A GENERATE
    XPath(x,'header/identifier'),
    XPathAll(x, 'record/metadata/article/front/article-meta/article-categories/subj-group/subject').$0 as subject1:chararray,
	XPathAll(x, 'record/metadata/article/front/article-meta/article-categories/subj-group/subject').$1 as subject2:chararray,
	XPathAll(x, 'record/metadata/article/front/article-meta/article-categories/subj-group/subject').$2 as subject3:chararray,
	XPathAll(x, 'record/metadata/article/front/article-meta/article-categories/subj-group/subject').$3 as subject4:chararray,
	XPathAll(x, 'record/metadata/article/front/article-meta/article-categories/subj-group/subject').$4 as subject5:chararray,
	XPathAll(x, 'record/metadata/article/front/article-meta/aff/country').$0 as location:chararray,
	XPath(x, 'record/metadata/article/front/journal-meta/publisher/publisher-loc') as locationjournal:chararray,
	XPath(x, 'record/metadata/article/front/article-meta/aff/institution') as institution:chararray,
	XPath(x, 'record/metadata/article/front/article-meta/pub-date/year') as year:int,
	XPath(x, 'record/metadata/article/front/article-meta/pub-date/month') as month:int,
    XPath(x,'metadata/article/front/journal-meta/journal-title-group/journal-title') as journaltitle:chararray,
    XPath(x, 'record/metadata/article/front/article-meta/title-group/article-title') as title,
    XPathAll(x,'metadata/article/front/journal-meta/publisher/publisher-name') as publishename:tuple(),
    XPathAll(x,'metadata/article/body/sec/p') as documentText1:tuple(),
    XPathAll(x,'metadata/article/body/sec/sec/p') as documentText2:tuple(),
    XPathAll(x,'metadata/article/body/p') as documentText3:tuple();


articole = FOREACH B GENERATE
        journaltitle,
		publishename,
		title,
		subject1,
		subject2,
		subject3,
		subject4,
		subject5,
		institution,
		location,
		locationjournal,
		year,
		month,
		documentText1,
		documentText2,
		documentText3
;

--dump B;
--store B into '/pmc_oa_processed' using PigStorage('#');
STORE articole INTO '/pmc_oa_test_data_processed'
	USING CSVExcelStorage(',','NO_MULTILINE','WINDOWS');