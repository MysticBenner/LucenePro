package com.itheima.lucene;

import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class SearchIndex {
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    @Before
    public void init() throws Exception{
        indexReader = DirectoryReader.open(FSDirectory.open(new File("D:\\index").toPath()));
        indexSearcher = new IndexSearcher(indexReader);
    }
    @Test
    public void testRangeQuery() throws Exception{
        Query query = LongPoint.newRangeQuery("size", 0l,50l);
        printResult(query);
    }

    private void printResult(Query query) throws IOException {
        //执行查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总记录数："+topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
//        - 打印文档中的内容
        for(ScoreDoc doc: scoreDocs){
            //取文档ID
            int docId = doc.doc;
            //根据id取文档对象
            Document document = indexSearcher.doc(docId);
            System.out.println("文件名："+document.get("name"));
            System.out.println("文件路径："+document.get("path"));
            System.out.println("文件大小："+document.get("size"));
            System.out.println("文件内容："+document.get("content"));
            System.out.println("----------------------结束分割线");
        }
//        - 关闭IndexReader对象
        indexReader.close();
    }
    @Test
    public void testQueryParse() throws Exception{
        //创建一个queryParse对象 参数1：默认搜索域，参数2：分析器对象
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        //使用QueryParse创建一个query对象
        Query query = queryParser.parse("中国伟大");
        //执行查询
        printResult(query);
    }
}
