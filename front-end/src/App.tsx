import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "./App.css";
import Dashboard from "./components/Dashboard";
import AuthProvider from "./contexts/AuthProvider";

function App() {
  const queryClient = new QueryClient();
  return (
    <div id="dashboard">
      <AuthProvider>
        <QueryClientProvider client={queryClient}>
          <Dashboard />
        </QueryClientProvider>
      </AuthProvider>
    </div>
  );
}

export default App;
