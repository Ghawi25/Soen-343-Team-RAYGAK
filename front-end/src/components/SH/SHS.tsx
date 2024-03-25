import axios from "axios";
import { useState } from "react";

function SHS() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");  

  const handleSubmit = (e: { preventDefault: () => void; }) => {
    e.preventDefault();
    axios.get(`http://localhost:8080/api/users/user/${username}`)
    .then((res) => {
      console.log(res);
    })
    .catch((err) => console.log(err));
  }

  return (
    <>
      <form
        id="login"
        className="bubble"
        onSubmit={handleSubmit}
      >
        <h5>Sign-In</h5>
        <div className="field">
          <label htmlFor="username">Username:</label>
          <input type="text" id="username" name="username" required onChange={(e) => setUsername(e.target.value)}/>
        </div>
        <div className="field">
          <label htmlFor="password">Password:</label>
          <input type="password" id="password" name="password" required onChange={(e) => setPassword(e.target.value)}/>
        </div>
        <div>
          <button type="submit">Sign In</button>
        </div>
      </form>
    </>
  );
}

export default SHS;
