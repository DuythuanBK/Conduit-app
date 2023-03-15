import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import fetchAPI from "../fetchAPI";

function Editor() {
  const params = useParams();
  const [article, setArticle] = useState(null);
  const navigate = useNavigate()

  useEffect(() => {
      fetchAPI.Article.getArticle(params.slug).then(data => setArticle(data.article));
  }, [params])

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
    setArticle({...article, tags: e.target.value})
  }

  function handleEditArticle(e) {
    e.preventDefault();
    fetchAPI.Article.updateArticle({article: article}).then(data => {
      navigate(`/article/${data.article.slug}`);
    })
    // fetchAPI.Article.create({article: article});
    

  }

  if(article === null) {
    return <></>
  }

  return (
    <div className="editor-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-10 offset-md-1 col-xs-12">
            <form>
              <fieldset>
                <fieldset className="form-group">
                  <input type="text" className="form-control form-control-lg" placeholder="Article Title" value={article.title} onChange={handleTilteChange}/>
                </fieldset>
                <fieldset className="form-group">
                  <input type="text" className="form-control" placeholder="What's this article about?" value={article.description} onChange={handleDescriptionChange}/>
                </fieldset>
                <fieldset className="form-group">
                  <textarea
                    className="form-control"
                    rows="8"
                    placeholder="Write your article (in markdown)"
                    value={article.body}
                    onChange={handleBodyChange}
                  ></textarea>
                </fieldset>
                <fieldset className="form-group">
                  <input type="text" className="form-control" placeholder="Enter tags"
                    value={article.tags} 
                    onChange={handleTagsChange}
                  />
                  <div className="tag-list"></div>
                </fieldset>
                <button className="btn btn-lg pull-xs-right btn-primary" type="submit"
                onClick={handleEditArticle}>
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

export default Editor;