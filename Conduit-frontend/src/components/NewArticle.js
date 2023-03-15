import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import fetchAPI from "../fetchAPI";

export default function NewArticle() {
  
  const [article, setArticle] = useState(null);
  const navigate = useNavigate()

  function handleTilteChange(e) {
    setArticle({...article, title: e.target.value});
  }

  function handleDescriptionChange(e) {
    setArticle({...article, description: e.target.value})
  }

  function handleBodyChange(e) {
    setArticle({...article, body: e.target.value})
  }

  function handleTagsChange(e) {
    setArticle({...article, tagList: e.target.value.split(',')})
  }

  function handleCreateArticle(e) {
    e.preventDefault();
    fetchAPI.Article.create({article: article});
    navigate(`/article/${article.slug}`);

  }

  return (
    <div className="editor-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-10 offset-md-1 col-xs-12">
            <form>
              <fieldset>
                <fieldset className="form-group">
                  <input type="text" className="form-control form-control-lg" placeholder="Article Title" onChange={handleTilteChange}/>
                </fieldset>
                <fieldset className="form-group">
                  <input type="text" className="form-control" placeholder="What's this article about?" onChange={handleDescriptionChange}/>
                </fieldset>
                <fieldset className="form-group">
                  <textarea
                    className="form-control"
                    rows="8"
                    placeholder="Write your article (in markdown)"
                    onChange={handleBodyChange}
                  ></textarea>
                </fieldset>
                <fieldset className="form-group">
                  <input type="text" className="form-control" placeholder="Enter tags"
                    onChange={handleTagsChange}
                  />
                  <div className="tag-list"></div>
                </fieldset>
                <button className="btn btn-lg pull-xs-right btn-primary" type="submit"
                onClick={handleCreateArticle}>
                  Publish Article
                </button>
              </fieldset>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}
