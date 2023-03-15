import { useEffect, useState } from "react";
import ArticlePreview from "./ArticlePreview";
import fetchAPI from "../fetchAPI";

function ArticleList({tab, page, tag, username=null, handleArticlesCountChange}) {
  const [articles, setArticles] = useState(null);
  const [status, setStatus] = useState('LOADING');

  useEffect(() => {
    setStatus('LOADING');
    if(tab === 'feed') {
      fetchAPI.Article.feed(page).then(data => { 
        setArticles(data.articles); 
        setStatus('LOADED');
        handleArticlesCountChange(data.articlesCount);
      });
    } else if(tab === 'all') {
      fetchAPI.Article.getAll(page).then(data => { 
        setArticles(data.articles);    
        setStatus('LOADED');          
        handleArticlesCountChange(data.articlesCount); 
      }); 
    } else if(tab === 'tag') {
      fetchAPI.Article.getAllByTag(tag, page).then(data => { 
        setArticles(data.articles);  
        setStatus('LOADED');            
        handleArticlesCountChange(data.articlesCount); 
      }); 
    } else if(tab === 'my') {
      fetchAPI.Article.getAllByAuthor(username, page).then(data => { 
        setArticles(data.articles);  
        setStatus('LOADED');            
        handleArticlesCountChange(data.articlesCount); 
      });
    } else if(tab === 'favorited') {
      fetchAPI.Article.getAllByFavorited(username, page).then(data => { 
        setArticles(data.articles);  
        setStatus('LOADED');            
        handleArticlesCountChange(data.articlesCount); 
      });
    } else {

    }

  }, [tab, page, tag]);

  if(articles === null) {
    return (
      <div className="article-preview">Loading articles...</div>
    )
  }
  if(articles.length === 0) {
    return (
      <>
        <div className="article-preview" >No articles are here... yet.</div>
        <div className="article-preview" style={{display: status === 'LOADING' ? '' : 'none'}}>Loading articles...</div>
      </>
    )
  }

  return (
    <>
    <div>
      {articles.map((article, index) => <ArticlePreview key={index} article={article}/>)}
    </div>
    <div className="article-preview" style={{display: status === 'LOADING' ? '' : 'none'}}>Loading articles...</div>
    </>
  )
}

export default ArticleList;