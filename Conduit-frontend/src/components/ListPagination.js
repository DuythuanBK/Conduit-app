function ListPagination({page, totalPage, handlePageChange}) {

  let range = [];
  for(let i = 0; i < totalPage; i++) {
    range.push(i);
  }

  function handleClick(e, nextPage) {
    e.preventDefault();
    handlePageChange(nextPage);
  }

  return (
    <nav>
      <ul className="pagination">
        {
          range.map(v => {
            return(
              <li key={v} className={page === v ? "page-item ng-scope active" : "page-item ng-scope"}>
                <a className="page-link ng-binding" href="" 
                  onClick={(e) => {handleClick(e, v)}}>{v+1}</a>
              </li>
            )
            }
          )
        }
      </ul>
    </nav>
  )
}

export default ListPagination;