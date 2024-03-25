function SHS() {
  return (
    <>
      <form
        id="login"
        action="/your-server-endpoint"
        method="post"
        className="bubble"
      >
        <h5>Sign-In</h5>
        <div className="field">
          <label htmlFor="username">Username:</label>
          <input type="text" id="username" name="username" required />
        </div>
        <div className="field">
          <label htmlFor="password">Password:</label>
          <input type="password" id="password" name="password" required />
        </div>
        <div>
          <button type="submit">Sign In</button>
        </div>
      </form>
    </>
  );
}

export default SHS;
