package com.itheima.lucene;

import com.sun.xml.internal.bind.api.impl.NameConverter;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.awt.*;
import java.io.File;

public class luceneFirst {
    @Test
    public void createIndex() throws Exception{
//    - 创建一个Directory对象，指定索引库保存的地址
        //把索引库保存在磁盘中
        Directory directory = FSDirectory.open(new File("D:\\index").toPath());
//    - 基于Directory对象创建一个Indexwrite对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
//    - 读取磁盘上的文件，对应每个文件创建一个对象
        File dir = new File("D:\\LearningNotes");
        File [] files = dir.listFiles();
        for(File f : files){
            //取文件名
            String fileName = f.getName();
            //文件的路径名
            String filePath = f.getPath();
            //文件的内容
            String fileContent = FileUtils.readFileToString(f,"utf-8");
            //文件大小
            long fileSize = FileUtils.sizeOf(f);
            //创建field
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            //Field fieldPath = new TextField("path", filePath, Field.Store.YES);
            Field fieldPath = new StoredField("path",filePath);
            Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
            //Field fieldSize = new TextField("size", fileSize+"", Field.Store.YES);
            Field fieldSizeValue = new LongPoint("size", fileSize);
            Field fieldSizeStore = new StoredField("size", fileSize);
            //创建文档对象
            Document document = new Document();
            //    - 向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSizeValue);
            document.add(fieldSizeStore);
            //    - 把文档对象写入索引库
            indexWriter.addDocument(document);
        }
//    - 关闭Indexwrite对象
        indexWriter.close();
    }
    @Test
    public void searchIndex() throws Exception{
//        - 创建一个Directory对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("D:\\index").toPath());
//        - 创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
//        - 创建一个IndexSearcher对象，构造方法中的参数indexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//        - 创建一个Query对象，TermQuery
        Query query = new TermQuery(new Term("content","jack"));
//        - 进行查询，得到一个TopDocs对象
        //参数1：查询对象；参数2：查询结果返回的最大记录数
        TopDocs topDocs = indexSearcher.search(query, 10);
//        - 取查询结果的总记录数
        System.out.println("查询总记录数："+topDocs.totalHits);
//        - 取文档列表
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
    public void testTokenStream() throws Exception{
//        - 创建一个Analyzer对象，StandardAnalyzer对象
        //Analyzer analyzer = new StandardAnalyzer();
        //Analyzer analyzer = new CJKAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
//        - 使用分析器对象的tokenStream方法获得一个TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("","中国伟大");
//        - 向TokenStream对象设置一个引用，相当于数一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
//        - 调用TokenStream对象的reset方法，如果不调用抛出异常
        tokenStream.reset();
//        - 使用while循环遍历TokenStream对象
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
//        - 关闭TokenStream对象
        tokenStream.close();
    }
}



























