import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import fetchAPI from "../fetchAPI";

function Register() {
  const navigate = useNavigate();
  const [user, setUser] = useState({username: "", email: "", password: ""});
  const [status, setStatus] = useState("none");
  const [errorMessage, setErrorMessage] = useState("");

  function handleEmailChange(e) {
    setUser({...user, email: e.target.value});
  }

  function handleUsernameChange(e) {
    setUser({...user, username: e.target.value});
  }

  function handlePasswordChange(e) {
    setUser({...user, password: e.target.value});
  }

  function handleSignupClick(e) {
    e.preventDefault();
    fetchAPI.Auth.register(user.username, user.email, user.password)
    .then(data => {
      if(data.hasOwnProperty("errors")) {
        setStatus("error");
        setErrorMessage(data.errors.body);
      } else {
        navigate("/login")
      }
    })
    
  }

  return (
    <div className="auth-page">
      <div className="container page">
        <div className="row">
          <div className="col-md-6 offset-md-3 col-xs-12">
            <h1 className="text-xs-center">Sign up</h1>
            <p className="text-xs-center">
              <Link to="/login">Have an account?</Link>
            </p>

            <ul className="error-messages disabled" style={{display: status === "error" ? "" : "none"}}>
              <li>{errorMessage}</li>
            </ul>

            <form onSubmit={handleSignupClick}>
              <fieldset className="form-group">
                <input className="form-control form-control-lg" type="text" placeholder="Your Name" required onChange={handleUsernameChange}/>
              </fieldset>
              <fieldset className="form-group">
                <input className="form-control form-control-lg" type="text" placeholder="Email" required onChange={handleEmailChange}/>
              </fieldset>
              <fieldset className="form-group">
                <input className="form-control form-control-lg" type="password" placeholder="Password" required onChange={handlePasswordChange}/>
              </fieldset>
              <button className="btn btn-lg btn-primary pull-xs-right">Sign up</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  )

}

export default Register;