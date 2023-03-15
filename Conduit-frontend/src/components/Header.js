import { useContext, useState } from "react";
import { Link } from "react-router-dom";
import { StateContext } from "../context";

function LoggedInView({currentUser, visitProfile}) {

  function handleUsernameClick(e) {
    visitProfile(currentUser);
  }

  if(currentUser) {
    return (
      <ul className="nav navbar-nav pull-xs-right">
          <li className="nav-item">
            <Link className="nav-link" to="/">Home</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/editor"> <i className="ion-compose"></i>&nbsp;New Article </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/settings"> <i className="ion-gear-a"></i>&nbsp;Settings </Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link ng-binding"  to={`/${currentUser.username}`} onClick={handleUsernameClick}>
              <img className="user-pic" src={currentUser.image}/>
              {currentUser.username}
            </Link>
          </li>
        </ul>
    )
  }

  return null;
}

function LoggedOutView({currentUser}) {
  if(!currentUser) {
    return (
      <ul className="nav navbar-nav pull-xs-right">
        <li className="nav-item">
          <Link className="nav-link" to="/">Home</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link" to="/login">Sign in</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link" to="/register">Sign up</Link>
        </li>
      </ul>
    )
  }

  return null;
}

function Header() {
  const [loggedIn, setLoggedIn] = useState(false);
  const state = useContext(StateContext);

  function visitProfile(user) {
  
  }

  return (
    <nav className="navbar navbar-light">
      <div className="container">
        <Link className="navbar-brand" to="/">conduit</Link>
        <LoggedInView currentUser={state.currentUser} visitProfile={visitProfile}/>
        <LoggedOutView currentUser={state.currentUser}/>
      </div>
    </nav>
  )
}

export default Header;
