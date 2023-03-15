// const API_ROOT = 'http://localhost:8080/api';
const API_ROOT = 'https://conduit.productionready.io/api';

const REQUEST_OPTIONS = {
  method: "",
  headers: {
    'Content-Type': 'application/json',
  },
}
let JWT_TOKEN = null

function token() {
  if(JWT_TOKEN === null) {
    return null;
  }

  return `Token ${JWT_TOKEN}`;
}

const requests = () => {
  let options = {};
  if(token() !== '') {
    options = {...REQUEST_OPTIONS, headers: {...REQUEST_OPTIONS.headers, "authorization": token()}};
  } else {
    options = {...REQUEST_OPTIONS, headers: {...REQUEST_OPTIONS.headers }};
  }
  
  return {
    del: url => {
      options = {...options, method:"DELETE"};
      return fetch(`${API_ROOT}${url}`, options);
    },
    get: url => {
      options = {...options, method:"GET"};
      return fetch(`${API_ROOT}${url}`, options);
    },
    put: (url, body) => {
      options = {...options, method:"PUT"};
      options.body = JSON.stringify({user: body});
      return fetch(`${API_ROOT}${url}`, options);
    },
    post: (url, body) => {
      options = {...options, method:"POST"};
      options.body = JSON.stringify(body);
      return fetch(`${API_ROOT}${url}`, options);
    }
  }
}

const Auth = {
  login: (email, password) => 
    requests().post('/users/login', {user: {email, password}}).then(response => response.json()),
  register: (username, email, password) =>
    requests().post('/users', { user: {username, email, password}}).then(response => response.json()),
  currentUser: () =>
    requests().get('/user').then(response => response.json()),
  update: (user) =>
    requests().put('/user', user).then(response => response.json()),
}

const Profile = {
  getProfile: (username) => 
    requests().get(`/profiles/${username}`).then(response => response.json()),
  follow: (username) => 
    requests().post(`/profiles/${username}/follow`).then(response => response.json()),
  unfollow: (username) => 
    requests().del(`/profiles/${username}/follow`).then(response => response.json()),
}

let limit = (count, p) => `limit=${count}&offset=${p ? p*count : 0}`;

const Article = {
  create: (article) => 
    requests().post('/articles', article).then(response => response.json()),
  getArticle: (slug) =>
    requests().get(`/articles/${slug}`).then(response => response.json()),
  updateArticle: (slug) =>
    requests().update(`/articles/${slug}`).then(response => response.json()),
  deleteArticle: (slug) =>
    requests().del(`/articles/${slug}`).then(response => response.json()),
  getAll: (page) => 
   requests().get(`/articles?${limit(10,page)}`).then(response => response.json()),
  feed: (page) => 
   requests().get(`/articles/feed?${limit(10,page)}`).then(response => response.json()),
  getAllByTag: (tag, page) =>
    requests().get(`/articles?${limit(5, page)}&tag=${tag}`).then(response => response.json()),
  getAllByAuthor: (author, page) =>
    requests().get(`/articles?author=${author}&${limit(5, page)}`).then(response => response.json()),
  getAllByFavorited: (author, page) =>
    requests().get(`/articles?favorited=${author}&${limit(5, page)}`).then(response => response.json()),
  favorite: (slug) =>
    requests().post(`/articles/${slug}/favorite`).then(response => response.json()),
  unfavorite: (slug) =>
    requests().del(`/articles/${slug}/favorite`).then(response => response.json()),
}

const Comment = {
  create: (slug, body) => 
    requests().post(`/articles/${slug}/comments`, body).then(response => response.json()),
  get: (slug) =>
    requests().get(`/articles/${slug}/comments`).then(response => response.json()),
  delete: (slug, id) => 
    requests().del(`/articles/${slug}/comments/${id}`),
}


const Tags = {
  getAll: () =>
    requests().get('/tags').then(response => response.json()),
}


export default {
  Auth,
  Tags,
  Article,
  Comment,
  Profile,
  setToken: _token => JWT_TOKEN = _token
};