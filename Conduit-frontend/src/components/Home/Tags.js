import { Link } from "react-router-dom";

function Tags({tags, handleTagClick}) {

  return(
    <div className="col-md-3">
      <div className="sidebar">
        <p>Popular Tags</p>
        {
          tags === null || tags.length === 0 ?
          <div>Loading...</div> 
          :
          <div className="tag-list">
            {
              tags.map((tag, index) => {
                return (
                  <Link key={index} className="tag-pill tag-default" to=""
                    onClick={() => handleTagClick(tag)}>{tag}</Link>
                )
              })
            }
          </div>
        }
      </div>
    </div>
  )
}

export default Tags;