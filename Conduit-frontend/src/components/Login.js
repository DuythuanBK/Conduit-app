import { useContext, useState } from "react";
import fetchApi from "../fetchAPI";
import { Link, useNavigate } from "react-router-dom";
import { StateDispatchContext } from "../context";

function Login() {
  const [status, setStatus] = useState("UNLOGED");
  const [user, setUser] = useState({email: "", password: ""});
  const [errorMsg, setErrorMsg] = useState("");
  const stateDispatch = useContext(StateDispatchContext);
  const navigate = useNavigate();

  function handleEmailChange(e) {
    setUser({...user, email: e.target.value});
  }

  function handlePasswordChange(e) {
    setUser({...user, password: e.target.value});
  }

  function handleSigninClick(e) {
    e.preventDefault();
    setStatus("LOGGING");
    fetchApi.Auth.login(user.email, user.password)
    .then(data => {
      if(data.hasOwnProperty('errors')){
        setStatus("ERROR");
        setErrorMsg(data.errors.body)
      } else {
        setStatus("LOGGED");
        fetchApi.setToken(data.user.token);
        let user = data.user;
        window.localStorage.setItem('jwt', user.token);
        stateDispatch({type: 'UPDATE_CURRENT_USER', user: user});
        navigate('/');
      }
    })
    .catch(error => {
      setStatus("ERROR");
    })

  }

  return (
    <div className="auth-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-6 offset-md-3 col-xs-12">
            <h1 className="text-xs-center">Sign in</h1>
            <p className="text-xs-center">
              <Link to="/register">Need an account?</Link>
            </p>

            <ul className="error-messages" style={{display: status === "ERROR" ? "" : "none"}}>
              <li>{errorMsg}</li>
            </ul>

            <form onSubmit={handleSigninClick}>
              <fieldset className="form-group">
                <input className="form-control form-control-lg" type="text" placeholder="Email" required onChange={handleEmailChange}/>
              </fieldset>
              <fieldset className="form-group">
                <input className="form-control form-control-lg" type="password" placeholder="Password" required onChange={handlePasswordChange}/>
              </fieldset>
              <button className="btn btn-lg btn-primary pull-xs-right" type="submit" disabled={status === "LOGGING"}>Sign in</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  )

}

export default Login;