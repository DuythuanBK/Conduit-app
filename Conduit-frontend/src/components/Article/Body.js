export default function Body({description, title, body}) {

  return (
    <div className="row article-content">
      <div className="col-md-12">
        {/* <p>{description}</p>
        <h2 id="introducing-ionic">{title}</h2> */}
        <p>{body}</p>
      </div>
    </div>
  )
}