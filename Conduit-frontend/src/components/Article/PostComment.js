import { useState } from "react";

function PostComment({image, handleAddComment}) {
  const [commentText, setCommentText] = useState('');

  function handleCommentTextChange(e) {
    setCommentText(e.target.value);
  }

  function handlePostCommentClick(e) {
    e.preventDefault();
    handleAddComment(commentText);
    setCommentText('');
  }

  return (
    <form className="card comment-form" onSubmit={handlePostCommentClick}>
      <div className="card-block">
        <textarea className="form-control" placeholder="Write a comment..." rows="3"
          onChange={handleCommentTextChange}></textarea>
      </div>
      <div className="card-footer">
        <img src={image} className="comment-author-img" />
        <button className="btn btn-sm btn-primary">Post Comment</button>
      </div>
    </form>
  )
}

export default PostComment;