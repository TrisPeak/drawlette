String host = "localhost:3469"; 


import processing.net.*; 

Client myClient; 

int incr = 10;  
void setup() { 
  size(800, 400); 
  background(255);   
  myClient = new Client(this, host.split(":")[0], Integer.parseInt(host.split(":")[1]));
}





char c = 'r';
void setColor(char co)
{
  c = co;
}

void draw() {    
  if (myClient.available() > 0) {  
    String in = myClient.readString(); 
    String[] b = in.split("-");
    if (b.length<2) return;
    float px = Float.parseFloat(b[0]) + width/2;

    float x = Float.parseFloat(b[1]) + width/2;
    float py = Float.parseFloat(b[2]) ;
    float y = Float.parseFloat(b[3]);

    char cc = b[4].charAt(0);

    switch(cc)
    {
    case 'r': 
      stroke(255, 100, 100); 
      break;   
    case 'g': 
      stroke(100, 255, 100); 
      break;   
    case 'b': 
      stroke(100, 100, 255); 
      break; 
    case 'y': 
      stroke(255, 210, 0); 
      break;   
    case 'w': 
      stroke(255); 
      break;   
    case 's': 
      stroke(0); 
      break;
    }
    strokeWeight(incr); 
    line(px, py, x, y); 
    if(ppx != 0)
    line(ppx, ppy, px, py);
    ppx = px;
    ppy = py;
  }
}

float limit(float x, float min, float max)
{
  if (x<min) return min;
  if (x > max) return max;
  return x;
}
float ppx, ppy;
void mouseDragged()
{
  if (mouseButton== LEFT) setColor('r'); 
  else setColor('w');
  setPixel(limit(pmouseX, 0, width/2), limit(pmouseY, 0, height), limit(mouseX, 0, width /2), limit(mouseY, 0, height), true);
}
void mouseClicked()
{
  if (mouseButton== LEFT) setColor('r'); 
  else setColor('w');
  setPixel(limit(pmouseX, 0, width/2), limit(pmouseY, 0, height), limit(mouseX, 0, width /2), limit(mouseY, 0, height), true);
}
float lx, ly;


void setPixel(float px, float py, float x, float y, boolean server)
{  
  px = round(px);
  py = round(py);
  x = round(x);
  y = round(y);


  strokeWeight(incr); 
  switch(c)
  {
  case 'r': 
    stroke(255, 100, 100); 
    break;   
  case 'g': 
    stroke(100, 255, 100); 
    break;   
  case 'b': 
    stroke(100, 100, 255); 
    break; 
  case 'y': 
    stroke(255, 210, 0); 
    break;   
  case 'w': 
    stroke(255); 
    break;   
  case 's': 
    stroke(0); 
    break;
  }
  line(px, py, x, y);
  if (server) {  
    myClient.write(""+px+"-"+x+"-"+py+"-"+y+"-"+c+"\n");
  }
}

void keyPressed()
{ 
  switch(key)
  {
      case 'r': 
  case 'g':  
  case 'b':  
  case 'y':  
  case 'w':  
  case 's':  
     setColor(key);
     println(c);
    break;
  }
}
