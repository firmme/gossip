package com.itheima.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.gossip.pojo.News;
import com.itheima.gossip.pojo.PageBean;
import com.itheima.gossip.pojo.ResultBean;
import com.itheima.search.service.IndexSearcherService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class IndexSearcherServiceImpl implements IndexSearcherService {
   @Autowired
    private SolrServer solrServer;

    @Override
    public ResultBean findByPage(ResultBean resultBean) throws Exception {


//        SolrQuery query = new SolrQuery("刘德华");
//        QueryResponse response = solrServer.query(query);
//        SolrDocumentList results = response.getResults();
//        for (SolrDocument result : results) {
//            String id = (String) result.get("id");
//            System.out.println(id + result);
//        }


        // //1. 封装查询的条件:  SolrQuery
        // 1.1 主入口的条件: 根据关键词查询
        SolrQuery solrQuery = new SolrQuery(resultBean.getKeywords());

        //1.2 分页的条件
        Integer page = resultBean.getPageBean().getPage();
        Integer pageSize = resultBean.getPageBean().getPageSize();

        solrQuery.setStart((page - 1) * pageSize);
        solrQuery.setRows(pageSize);
        //1.3添加高亮设置
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        //2. 执行查询操作
        QueryResponse response1 = solrServer.query(solrQuery);
        //fengzhuangshuju
        PageBean pageBean = resultBean.getPageBean();
        SolrDocumentList documentList = new SolrDocumentList();
       //将高亮的结果封装到Bean中;
        Map<String, Map<String,List<String>>> highLighting = response1.getHighlighting();//高亮的结果
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<News> newList = new ArrayList<News>();
        System.out.println(documentList);
        for (SolrDocument document : documentList) {

            String id = (String) document.get("id");
            String title = (String) document.get("title");
            Date date = (Date) document.get("time");
            String source = (String) document.get("source");
            String content = (String) document.get("content");
            String editor = (String) document.get("editor");
            String docurl = (String) document.get("docurl");

            //有高亮的内容
            Map<String, List<String>> stringListMap = highLighting.get(id);
            List<String> list = stringListMap.get("title");
            //判断是否有高亮
            if(list != null&&list.size()>0){
                title=list.get(0);
            }
            list = stringListMap.get("content");
            if(list !=null&&list.size()>0){
                content = list.get(0);
            }else{
                //如果没有高亮.应对原数据进行截取处理
                int size = content.length();
                if(size>100){
                    content=content.substring(0,99)+"......";
                }
            }
            News news = new News();
            news.setId(id);
            news.setContent(content);
            news.setDocurl(docurl);
            news.setEditor(editor);
            news.setSource(source);
            String newtime = sdf.format(date);
            news.setTime(newtime);
            news.setTitle(title);

            newList.add(news);
        }
        pageBean.setNewsList(newList);
        //3.2 总条数封装
        Long pageCount = documentList.getNumFound();
        pageBean.setPageCount(pageCount.intValue());
        //3.3 封装 总页数:
        Double pageNumber = Math.ceil((double) pageCount / pageSize);

        pageBean.setPageNumber(pageNumber.intValue());


        //4. 将封装好pageBean对象, 设置到resultBean中
        resultBean.setPageBean(pageBean);

        return resultBean;
    }
}
