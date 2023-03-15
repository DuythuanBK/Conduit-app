import PostComment from "./PostComment";
import Comment from "./Comment";
import Body from "./Body";
import Meta from "./Meta";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import fetchAPI from "../../fetchAPI";
import { StateContext } from "../../context";

function CommentUnavailable({currentUser}) {
  if(!currentUser) {
    return (
      <div className="col-xs-12 col-md-8 offset-md-2">
        <Link to="/login">Sign in </Link> 
        or 
        <Link to="/register"> sign up</Link> 
        to add comments on this article.
      </div>
    );
  }

  return null;
}


function CommentAvailable({currentUser, article, comments, handleAddComment, handleDeleteComment}) {
  if(currentUser) {
    return (
      <div className="col-xs-12 col-md-8 offset-md-2">
        <PostComment image={article.author.image} handleAddComment={handleAddComment}/>
        {
          !comments ? 
          <></> :
          comments.map((comment, index) => {
            return <Comment key={index} comment={comment} 
            handleDeleteComment={(commentId) => { handleDeleteComment(commentId) }}/>
          })
        } 
      </div>
    );
  }

  return null;
}

function Article() {
  const params = useParams();
  const state = useContext(StateContext);
  const navigate = useNavigate();
  const [article, setArticle] = useState(null);
  const [comments, setComments] = useState([]);

  useEffect(() => {
    fetchAPI.Article.getArticle(params.slug).then(data => {setArticle(data.article)});
    fetchAPI.Comment.get(params.slug).then(data => setComments(data.comments))
  }, [params])

  function handleAddComment(commentBody) {
    let comment = {
      comment: {
        body: commentBody
      }
    }
    fetchAPI.Comment.create(params.slug, comment).then(data => {setComments([...comments, data.comments])});
  }

  function handleDeleteComment(commentId) {
    fetchAPI.Comment.delete(params.slug, commentId).then(() => {setComments(comments.filter(comment => comment.id !== commentId));});
    
  }

  function handleFavorite() {
    if(state.currentUser === null) {
      navigate('/login');
    } else {
      if(article.favorited) {
        fetchAPI.Article.unfavorite(article.slug).then(data => {
          setArticle(data.article)
          
        });
      } else {
        fetchAPI.Article.favorite(article.slug).then(data => {
          setArticle(data.article)
        });
      }
    }
  }

  function handleFollow() {
    if(state.currentUser === null) {
      navigate('/login');
    } else {
      if(article.author.following) {
        fetchAPI.Profile.unfollow(article.author.username).then(data => setArticle({...article, author: data.profile}));
      } else {
        fetchAPI.Profile.follow(article.author.username).then(data => setArticle({...article, author: data.profile}));
      }
    }
  }

  if(article === null || !article) {
    return <></>
  }

  return (
    <div className="article-page">
      <div className="banner">
        <div className="container">
          <h1>{article.title}</h1>
          <Meta article={article} handleFavorite={handleFavorite} handleFollow={handleFollow}
          />
        </div>
      </div>

      <div className="container page">
        <Body description={article.description} title={article.title} body={article.body}/>
        <hr />
        <div className="article-actions">
          <Meta article={article} handleFavorite={handleFavorite} handleFollow={handleFollow}/>
        </div>
        <div className="row">
          <CommentAvailable currentUser={state.currentUser} article={article} comments={comments}
            handleAddComment={handleAddComment} handleDeleteComment={handleDeleteComment}
          />
          <CommentUnavailable currentUser={state.currentUser}/>
        </div>

      </div>
    </div>
  )
}

export default Article;