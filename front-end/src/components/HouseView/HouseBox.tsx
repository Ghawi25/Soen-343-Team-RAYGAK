import axios from "axios";
import { useEffect, useState } from "react";

// function to generate a rectangle for the room
const Room = ({ name, x, y, width, height }) => {
  return (
    <g>
        <rect x={x*width*100} y={y*height*100} width={width*100} height={height*100} fill="white" stroke="black"/>
        <text x={x*width*100 + width*10} y={y*height*100 + height*50} font-family="Verdana" font-size="35" fill="black">{name}</text>
    </g>)
}

// Function to render all rooms
function renderRooms(rooms) {
  // Get positions first
  var positions = {};
  // Start at 0, 0
  var x = 0;
  var y= 0;
  positions[rooms[0].roomID] = {x: x, y: y}
  rooms.forEach((room) => {
    var currRoomPositions = positions[room.roomID];
    var adjacentRoom = room.topAdjacentRoom;
    if(!(adjacentRoom === null)) {
      positions[adjacentRoom.roomID] = {x: currRoomPositions.x, y: currRoomPositions.y-1}
    }
    adjacentRoom = room.bottomAdjacentRoom;
    if(!(adjacentRoom === null)) {
      positions[adjacentRoom.roomID] = {x: currRoomPositions.x, y: currRoomPositions.y+1}
    }
    adjacentRoom = room.leftAdjacentRoom;
    if(!(adjacentRoom === null)) {
      positions[adjacentRoom.roomID] = {x: currRoomPositions.x-1, y: currRoomPositions.y}
    }
    adjacentRoom = room.rightAdjacentRoom;
    if(!(adjacentRoom === null)) {
      positions[adjacentRoom.roomID] = {x: currRoomPositions.x+1, y: currRoomPositions.y}
    }    
  })
  console.log(positions);
  // calculating offsets to make turn all positions positive
  var minX = Infinity;
  var minY = Infinity;
  for (let [key, value] of Object.entries(positions)) {
    if (value.x < minX) minX = value.x;
    if (value.y < minY) minY = value.y;
  };

  // apply offset
  for (let [key, value] of Object.entries(positions)) {
    value.x = value.x - minX;
    value.y = value.y - minY;
  };

  return rooms.map(room => {
    const roomPositions = positions[room.roomID];
    return <Room name={room.name} x={roomPositions.x} y={roomPositions.y} width={room.width} height={room.height}/>
  });
}

function HouseBox() {
  const [house, setHouse] = useState(null);

  // Get house information
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/house")
      .then((res) => setHouse(res.data))
      .catch((err) => console.log(err));
  }, []);

  if (!house) {
    return <div>Loading...</div>
  } else {
    const rooms = house.rooms;
    return (
      <svg width="100%" height="100%">
        {renderRooms(rooms)}
      </svg>
    )
  }
}

export default HouseBox;
