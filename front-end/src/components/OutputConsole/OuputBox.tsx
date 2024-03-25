import { useQuery } from "@tanstack/react-query";
import axios from "axios";

function OutputBox() {
  // API logic
  const getLogs = async () => {
    const response = await axios.get(
      `http://localhost:8080/api/house/temperature`
    );
    return response.data;
  };
  // Queries
  const { data } = useQuery({
    queryKey: ["logs"],
    queryFn: getLogs,
    refetchInterval: 1000,
  });

  return (
    <div>
      <ul>
        {data?.logMsg.map((log: string, index: number) => (
          <li key={index}>{log}</li>
        ))}
      </ul>
    </div>
  );
}

export default OutputBox;
