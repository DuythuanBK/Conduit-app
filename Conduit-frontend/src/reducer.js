function stateReducer(state, action) {
  switch(action.type) {
    case 'UPDATE_CURRENT_USER': {
      return {
        ...state,
        currentUser: action.user
      };
    }

    default: {
      console.log("Error: Action not found");
    }
  }
}


export default stateReducer;