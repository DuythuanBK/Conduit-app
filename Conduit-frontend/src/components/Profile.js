import { useEffect, useState, useContext } from "react";
import { Link, useParams } from "react-router-dom";
import { StateContext } from "../context";
import fetchAPI from "../fetchAPI";
import ArticleList from "./ArticleList";
import ListPagination from "./ListPagination";

function MyArticleTab({tab, handleTabChange}) {
  const user = useContext(StateContext);

  if(user) {
    function handleClick(e) {
      e.preventDefault();
      handleTabChange('my');
    }

    return(
      <li className="nav-item">
        <a className={tab === 'my' ? "nav-link active" : "nav-link"} href=""
          onClick={handleClick}>My Article</a>
      </li>
    )
  } 
  return null;
}

function FavoritedArticleTab({tab, handleTabChange}) {

  function handleClick(e) {
    e.preventDefault();
    handleTabChange('favorited');
  }

  return(
    <li className="nav-item">
      <a className={tab === 'favorited' ? "nav-link active" : "nav-link"} href=""
        onClick={handleClick}>Favorited Article</a>
    </li>
  )
}

function Profile(props) {
  const params = useParams();
  const state = useContext(StateContext);
  const [tab, setTab] = useState('my');
  const [page, setPage] = useState(0);
  const [articlesCount, setArticlesCount] = useState(0);
  const [profile, setProfile] = useState(null);
  
  useEffect(() => {
    fetchAPI.Profile.getProfile(params.username).then(data => {setProfile(data.profile)});
    setTab('my');
  }, [params])

  function handleFollowClick() {
    if(profile.following) {
      fetchAPI.Profile.unfollow(profile.username).then(data => {setProfile(data.profile)})
    }else {
      fetchAPI.Profile.follow(profile.username).then(data => {setProfile(data.profile)})
    }
  }

  function handleTabChange(_tab) {
    setTab(_tab);
  }

  function handlePageChange(page) {
    setPage(page);
  }

  function handleArticlesCountChange(count) {
    setArticlesCount(0);
  }

  if(profile === null) {
      return <div></div>
  }

  return (
    <div className="profile-page">
      <div className="user-info">
        <div className="container">
          <div className="row">
            <div className="col-xs-12 col-md-10 offset-md-1">
              <img src={profile.image} className="user-img" />
              <h4>{profile.username}</h4>
              <p>{profile.bio}</p>
              {!(state.currentUser.username === profile.username) ?
              <button className="btn btn-sm btn-outline-secondary action-btn"
                onClick={handleFollowClick}>
                <i className="ion-plus-round"></i>
                &nbsp; {profile.following ? "Unfollow Eric Simons" : "Follow Eric Simons"}
              </button> : 
              <Link className="btn btn-sm btn-outline-secondary" to={`/settings`}>
                <i className="ion-gear-a"></i>
                Edit Profile Settings
              </Link>
              }
            </div>
          </div>
        </div>
      </div>

      <div className="container">
        <div className="row">
          <div className="col-xs-12 col-md-10 offset-md-1">
            <div className="articles-toggle">
              <ul className="nav nav-pills outline-active">
                <MyArticleTab tab={tab} handleTabChange={handleTabChange}/>
                <FavoritedArticleTab tab={tab} handleTabChange={handleTabChange}/>
              </ul>
            </div>
            <ArticleList tab={tab} page={page} username={profile.username} handleArticlesCountChange={handleArticlesCountChange} />
            <ListPagination page={page} totalPage={articlesCount} handlePageChange={handlePageChange}/>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Profile;