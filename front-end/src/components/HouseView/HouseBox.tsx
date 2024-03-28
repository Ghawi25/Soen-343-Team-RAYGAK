import axios from "axios";
import { useEffect, useState } from "react";

var roomPositions = {}
// function to generate a rectangle for the room
const Room = ({ name, x, y, width, height, id}) => {
  return (
    <g key={id}>
        <rect x={x*width*100} y={y*height*100} width={width*100} height={height*100} fill="white" stroke="black"/>
        <text x={x*width*100 + width*10} y={y*height*100 + height*50} font-family="Verdana" font-size="35" fill="black">{name}</text>
    </g>)
}

// Function to render all rooms
function renderRooms(rooms) {
  // Get positions first
  roomPositions = {};
  // Start at 0, 0
  var x = 0;
  var y= 0;
  roomPositions[rooms[0].roomID] = {x: x, y: y}
  rooms.forEach((room) => {
    var currRoomPositions = roomPositions[room.roomID];
    var adjacentRoom = room.topAdjacentRoom;
    if(!(adjacentRoom === null)) {
      roomPositions[adjacentRoom.roomID] = {x: currRoomPositions.x, y: currRoomPositions.y-1}
    }
    adjacentRoom = room.bottomAdjacentRoom;
    if(!(adjacentRoom === null)) {
      roomPositions[adjacentRoom.roomID] = {x: currRoomPositions.x, y: currRoomPositions.y+1}
    }
    adjacentRoom = room.leftAdjacentRoom;
    if(!(adjacentRoom === null)) {
      roomPositions[adjacentRoom.roomID] = {x: currRoomPositions.x-1, y: currRoomPositions.y}
    }
    adjacentRoom = room.rightAdjacentRoom;
    if(!(adjacentRoom === null)) {
      roomPositions[adjacentRoom.roomID] = {x: currRoomPositions.x+1, y: currRoomPositions.y}
    }    
  })
  console.log(roomPositions);
  // calculating offsets to make turn all positions positive
  var minX = Infinity;
  var minY = Infinity;
  for (let [key, value] of Object.entries(roomPositions)) {
    if (value.x < minX) minX = value.x;
    if (value.y < minY) minY = value.y;
  };

  // apply offset
  for (let [key, value] of Object.entries(roomPositions)) {
    value.x = value.x - minX;
    value.y = value.y - minY;
  };

  return rooms.map(room => {
    const roomPosition = roomPositions[room.roomID];
    return <Room name={room.name} x={roomPosition.x} y={roomPosition.y} width={room.width} height={room.height} id={room.roomID}/>
  });
}

// Render all doors
function renderDoors(rooms) {
  console.log(rooms);
  // TODO get offset of doors
  rooms.forEach(room => {
    
  });
}

// TODO render windows
// TODO render lights

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
    return (
      <svg width="100%" height="100%">
        {renderRooms(house.rooms)}
        {renderDoors(house.rooms.doors)}
      </svg>
    )
  }
}

export default HouseBox;
