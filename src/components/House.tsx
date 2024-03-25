import axios from "axios";
import { useEffect, useState } from "react";

function House() {
  const [houseJSON, setHouseJSON] = useState({});

  // Load json house if its not blank
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/house")
      .then((res) => setHouseJSON(res.data))
      .catch((err) => console.log(err));
  }, []);

  return <div>House</div>;
}

export default House;
