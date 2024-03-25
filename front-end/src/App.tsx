import "./App.css";
import Dashboard from "./components/Dashboard";
import AuthProvider from "./contexts/AuthProvider";

function App() {
  return (
    <div id="dashboard">
      <AuthProvider>
        <Dashboard />
      </AuthProvider>
    </div>
  );
}

export default App;
