import { useEffect, useReducer, useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Article from "./components/Article/Article";
import Editor from "./components/Editor";
import Header from "./components/Header";
import Home from "./components/Home/Home";
import Login from "./components/Login";
import Profile from "./components/Profile";
import ProfileFavorites from "./components/ProfileFavorites";
import Register from "./components/Register";
import Settings from "./components/Setting";
import fetchAPI from "./fetchAPI";
import stateReducer from "./reducer";
import { StateContext, StateDispatchContext } from "./context";
import NewArticle from "./components/NewArticle";

const initialState = {
  status: 'UNLOADED',
  currentUser: null
}


function App() {
  const [state, stateDispatch] = useReducer(stateReducer, initialState); 
  const [status, setStatus] = useState("UNLOADED");

  useEffect(() => {
    let token = window.localStorage.getItem('jwt');
    if(status === "UNLOADED" && token !== null) {
      fetchAPI.setToken(token);
      fetchAPI.Auth.currentUser().then(data => { stateDispatch({type:'UPDATE_CURRENT_USER', user: data.user})});
    }
    setStatus("LOADED")
  }, [status]);

  return (
    <BrowserRouter>
    <StateContext.Provider value={state}>
      <StateDispatchContext.Provider value={stateDispatch}>
        <Header />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<Home />} />
          <Route path="/article/:slug" element={<Article />} />
          <Route path="/editor" element={<NewArticle />} />
          <Route path="/editor/:slug" element={<Editor/>} />
          <Route path="/settings" element={<Settings />} />
          <Route path="/:username" element={<Profile />} />
          <Route path="/:username/favorites" element={<ProfileFavorites />} />
        </Routes>
      </StateDispatchContext.Provider>
    </StateContext.Provider>
    </BrowserRouter>
  );
}

export default App;
