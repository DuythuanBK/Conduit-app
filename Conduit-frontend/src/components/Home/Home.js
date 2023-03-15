import { useState, useEffect, useContext } from "react";
import { StateContext } from "../../context";
import fetchAPI from "../../fetchAPI";
import ArticleList from "../ArticleList";
import ListPagination from "../ListPagination";
import Banner from "./Banner";
import Tags from "./Tags";

function YourFeedTab({status, handleStatusChange}) {
  const state = useContext(StateContext);

  if(state.currentUser) {

    function handleClick(e) {
      e.preventDefault();
      handleStatusChange('feed');
    }

    return(
      <li className="nav-item">
        <a className={status === 'feed' ? "nav-link active" : "nav-link"}
          onClick={handleClick}>Your Feed</a>
      </li>
    )
  } 

  return null;
}

function GlobalFeedTab({status, handleStatusChange}) {

  function handleClick(e) {
    e.preventDefault();
    handleStatusChange('all');
  }

  return(
    <li className="nav-item">
      <a className={status === 'all' ? "nav-link active" : "nav-link"} href=""
        onClick={handleClick}>Global Feed</a>
    </li>
  )
}

function TagFeedTab({status, tag}) {
  if(status !== 'tag') {
    return null;
  }

  return(
    <li className="nav-item">
      <a className="nav-link active" href="">{`#${tag}`}</a>
    </li>
  )
}

function Home() {
  const [tags, setTags] = useState([]);
  const [articlesCount, setArticlesCount] = useState(0);
  const state = useContext(StateContext);
  const [status, setStatus] = useState(null);
  const [page, setPage] = useState(0);
  const [tag, setTag] = useState(null);

  useEffect(() => {
    fetchAPI.Tags.getAll().then(data => { setTags(data.tags)} );
    if(state.currentUser === null) {
      setStatus('all');
    } else {
      setStatus('feed');
    }
  }, []);

  function handleStatusChange(status) {
    setPage(0);
    setStatus(status);
  }

  function handlePageChange(page) {
    setPage(page);
  }

  function handleArticlesCountChange(count) {
    setArticlesCount(count);
  }

  function handleTagClick(tag) {
    setPage(0);
    setTag(tag);
    setStatus('tag');
  }
  
  return (
    <div className="home-page">
      <Banner />
      <div className="container page">
        <div className="row">
          <div className="col-md-9">
            <div className="feed-toggle">
              <ul className="nav nav-pills outline-active">
                <YourFeedTab status={status}  handleStatusChange={handleStatusChange}/>
                <GlobalFeedTab status={status} handleStatusChange={handleStatusChange}/>
                <TagFeedTab status={status} tag={tag}/>
              </ul>
            </div>
            <ArticleList tab={status} page={page} tag={tag} handleArticlesCountChange={handleArticlesCountChange} />
            <ListPagination page={page} totalPage={articlesCount/10} handlePageChange={handlePageChange}/>
          </div>
          <Tags tags={tags} handleTagClick={handleTagClick}/>
        </div>
      </div>
    </div>
  ) 
}


export default Home;