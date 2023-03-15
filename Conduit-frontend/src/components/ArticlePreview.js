import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { StateContext } from "../context";
import fetchAPI from "../fetchAPI";

const monthNames = ["January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"
];

function ArticlePreview({article}) {
  const state = useContext(StateContext);
  const navigate = useNavigate();
  const [favorite, setFavorite] = useState(article.favorited);
  
  function getDate(dateString) {
    let date = new Date(dateString);
    let year = date.getFullYear();
    let day = date.getDate();
    let month = monthNames[date.getMonth()];
    
    return `${month} ${day}, ${year}`;
  }

  function handleFavoriteClick() {
    if(state.currentUser === null) {
      navigate("/login");
    } else {
      if(favorite) {
        fetchAPI.Article.unfavorite(article.slug);
      } else {
        fetchAPI.Article.favorite(article.slug);
      }
      setFavorite(!favorite)
    }
  }

  return(
    <div className="article-preview">
      <div className="article-meta">
        <Link to={`/${article.author.username}`}><img src={article.author.image} /></Link>
        <div className="info">
          <Link to={`/${article.author.username}`} className="author">{article.author.username}</Link>
          <span className="date">{getDate(article.createdAt)}</span>
        </div>
        <button className={`btn btn-outline-primary btn-sm pull-xs-right ${favorite ? "active" : ""}`} onClick={handleFavoriteClick}>
          <i className="ion-heart"></i> {article.favoritesCount}
        </button>
      </div>
      <Link to={`/article/${article.slug}`} className="preview-link">
        <h1>{article.title}</h1>
        <p>{article.description}</p>
        <span>Read more...</span>
        <ul className="tag-list">
          {
            article.tagList.map((tag, index) => {
              return <li key={index} className="tag-default tag-pill tag-outline ng-binding ng-scope">{tag}</li>
            })
          }
        </ul>
      </Link>
    </div>
  )
}

export default ArticlePreview;