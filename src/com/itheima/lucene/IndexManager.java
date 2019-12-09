package com.itheima.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class IndexManager {
    private IndexWriter indexWriter;
    @Before
    public void init() throws Exception{
        //创建一个IndexWriter对象，需要用IKAnalyzer作为分析器
        indexWriter = new IndexWriter(FSDirectory.open(new File("D:\\index").toPath()), new IndexWriterConfig(new IKAnalyzer()));
    }
    @Test
    public void addDocument() throws Exception{
        //创建一个IndexWriter对象，需要用IKAnalyzer作为分析器
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File("D:\\index").toPath()), new IndexWriterConfig(new IKAnalyzer()));
        //创建一个document对象
        Document document = new Document();
        //向document对象中添加域
        document.add(new TextField("name", "新添加的文件", Field.Store.YES));
        document.add(new TextField("content", "新添加的文件内容", Field.Store.YES));
        document.add(new StoredField("path", "D:\\LearningNotes"));
        //把文档写入索引库
        indexWriter.addDocument(document);
        //关闭索引库
        indexWriter.close();
    }
    @Test
    public void deleteAllDocument() throws Exception{
        //删除全部
        indexWriter.deleteAll();
        //关闭索引库
        indexWriter.close();
    }
    @Test
    public void deleteDocumentByQuery() throws Exception{
        indexWriter.deleteDocuments(new Term("content", "jack"));
        indexWriter.close();
    }
    @Test
    public void updateDocument() throws Exception{
        //创建一个新的文档
        Document document = new Document();
        //向文档对象中添加域
        document.add(new TextField("name","chen",Field.Store.YES));
        document.add(new TextField("name1","wang",Field.Store.YES));
        document.add(new TextField("name2","li",Field.Store.YES));
        //更新操作
        indexWriter.updateDocument(new Term("content","jackmary"), document);
        //关闭索引库
        indexWriter.close();
    }
}
