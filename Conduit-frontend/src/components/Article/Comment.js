
const monthNames = ["January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"
];

function Comment({comment, handleDeleteComment}) {

  function getDate(dateString) {
    let date = new Date(dateString);
    let year = date.getFullYear();
    let day = date.getDate();
    let month = monthNames[date.getMonth()];

    return `${month} ${day}, ${year}`;
  }

  return (
    <div className="card">
      <div className="card-block">
        <p className="card-text">
          {comment.body}
        </p>
      </div>
      <div className="card-footer">
        <a href="" className="comment-author">
          <img src={comment.author.image} className="comment-author-img" />
        </a>
        &nbsp;
        <a href="" className="comment-author">{comment.author.username}</a>
        <span className="date-posted">{getDate(comment.createdAt)}</span>
        <span className="mod-options">
          {/* <i className="ion-edit"></i> */}
          <i className="ion-trash-a" onClick={() => {handleDeleteComment(comment.id)}}></i>
        </span>
      </div>
    </div>
  )
}

export default Comment;