import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { StateContext, StateDispatchContext } from "../context";
import fetchAPI from "../fetchAPI";


function Settings() {
  const state = useContext(StateContext);
  const [user, setUser] = useState({...state.currentUser});
  const stateDispatch = useContext(StateDispatchContext);
  const navigate = useNavigate();

  function updateUser(field) {
    return (e) => {
      setUser({...state.currentUser, [field]: e.target.value})
    }
  }

  function handleUpdateClick(e) {
    e.preventDefault();
    fetchAPI.Auth.update(user);
    stateDispatch({type: 'UPDATE_CURRENT_USER', user: user});
    navigate(`/${user.username}`);
  }

  function handleLogoutClick(e) {
    e.preventDefault();
    window.localStorage.removeItem('jwt');
    fetchAPI.setToken(null);
    stateDispatch({type: 'UPDATE_CURRENT_USER', user: null});
    navigate('/');
  }

  return (
    <div className="settings-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-6 offset-md-3 col-xs-12">
            <h1 className="text-xs-center">Your Settings</h1>

            <form onSubmit={handleUpdateClick}>
              <fieldset>
                <fieldset className="form-group">
                  <input className="form-control" type="text" placeholder="image" value={user.image} 
                  onChange={updateUser('image')}/>
                </fieldset>
                <fieldset className="form-group">
                  <input className="form-control form-control-lg" type="text" placeholder="Username" value={user.username} 
                  onChange={updateUser('username')}/>
                </fieldset>
                <fieldset className="form-group">
                  <textarea
                    className="form-control form-control-lg"
                    rows="8"
                    placeholder="Short bio about you"
                  ></textarea>
                </fieldset>
                <fieldset className="form-group">
                  <input className="form-control form-control-lg" type="text" placeholder="Email" value={user.email} 
                  onChange={(e) => updateUser(e, 'email')}/>
                </fieldset>
                <fieldset className="form-group">
                  <input className="form-control form-control-lg" type="password" placeholder="Password" value={user.password} 
                  onChange={updateUser('password')}/>
                </fieldset>
                <button className="btn btn-lg btn-primary pull-xs-right">Update Settings</button>
              </fieldset>
            </form>
            <hr />
            <button className="btn btn-outline-danger" onClick={handleLogoutClick}>Or click here to logout.</button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Settings;