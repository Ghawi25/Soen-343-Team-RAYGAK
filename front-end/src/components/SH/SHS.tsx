import { useState } from "react";
import { userAuth } from "../../contexts/AuthProvider";

function SHS() {
  const auth = userAuth(); // Add this if you want to access user info once logged in
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e: { preventDefault: () => void }) => {
    e.preventDefault();
    const data = {
      username: username,
      password: password,
    };
    auth.loginAction(data);
  };

  if (auth.user) {
    // Add what the user should see in the shs tab once logged in
    return <div>Logged in as {auth.user.username}</div>;
  } else {
    return (
      <>
        <form id="login" className="bubble" onSubmit={handleSubmit}>
          <h5>Sign-In</h5>
          <div className="field--login">
            <label htmlFor="username">Username:</label>
            <input
              type="text"
              id="username"
              name="username"
              required
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className="field--login">
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              id="password"
              name="password"
              required
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <div>
            <button type="submit">Sign In</button>
          </div>
        </form>
      </>
    );
  }
}

export default SHS;
