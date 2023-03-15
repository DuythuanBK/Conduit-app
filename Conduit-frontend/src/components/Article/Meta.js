import { useContext } from "react";
import { Link } from "react-router-dom";
import { StateContext } from "../../context";
import fetchAPI from "../../fetchAPI";

const monthNames = ["January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"
];


function CurrentUserMeta({slug}) {

  function handleDeleteClick() {
    fetchAPI.Article.deleteArticle(slug);
  }

  return(
    <>
      <button className="btn btn-sm btn-outline-primary">
        <i className="ion-edit"></i>
        &nbsp; <Link to={`/editor/${slug}`}>Edit Article</Link>
      </button>
      &nbsp;
      <button className="btn btn-sm btn-outline-primary"
      onClick={handleDeleteClick}>
        <i className="ion-heart"></i>
        &nbsp; <Link to="/">Delete Article</Link>
      </button>
    </>
  )
}

function VisitingUserMeta({article, handleFavorite, handleFollow}) {
  return(
    <>
      <button className={`btn btn-sm btn-outline-primary ${article.author.following ? "active": ""}`}
        onClick={handleFollow}>
        <i className="ion-plus-round"></i>
        &nbsp; {article.author.following ? `Unfollow ${article.author.username}` :`Follow ${article.author.username}`}
      </button>
      &nbsp;
      <button className={`btn btn-sm btn-outline-primary ${article.favorited ? "active": ""}`}
      onClick={handleFavorite}>
        <i className="ion-heart"></i>
        &nbsp; {article.favorited ? "Unfavorite Post" :"Favorite Post"} <span className="counter">({article.favoritesCount})</span>
      </button>
    </>
  )
}

function Meta({article, handleFavorite, handleFollow}) {
  const state = useContext(StateContext)

  function getDate(dateString) {
    let date = new Date(dateString);
    let year = date.getFullYear();
    let day = date.getDate();
    let month = monthNames[date.getMonth()];

    return `${month} ${day}, ${year}`;
  }
  
  function handleFavoriteClick() {
    handleFavorite();
  }

  function handleFollowClick() {
    handleFollow();
  }

  if(!article || article === null) {
    return <></>
  }

  return (
    <div className="article-meta">
      <a href=""><img src={article.author.image} /></a>
      <div className="info">
        <a href="/" className="author">{article.author.username}</a>
        <span className="date">{getDate(article.createdAt)}</span>
      </div>
      {
        state.currentUser === null ?
        <VisitingUserMeta article={article} handleFavorite={handleFavoriteClick} handleFollow={handleFollowClick}/>
        :
        state.currentUser.username === article.author.username ?
        <CurrentUserMeta slug={article.slug}/> :
        <VisitingUserMeta article={article} handleFavorite={handleFavoriteClick} handleFollow={handleFollowClick}/>
      }
    </div>
  )
}

export default Meta;