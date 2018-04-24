function polygonArea(points)
{
    var i, j;
    var area = 0;
    for (i = 0; i < points.length; i++)
    {
        j = (i + 1) % points.length;
        area += points[i].x * points[j].y;
        area -= points[i].y * points[j].x;
    }
    area /= 2;
    return Math.abs(area);
}

// 多边形面积公式说明：
//
// 我们都知道已知A(x1,y1)、B(x2,y2)、C(x3,y3)三点的面积公式 |x1 x2 x3| 为：
// S(A,B,C) = |y1 y2 y3| * 0.5 = [(x1-x3)*(y2-y3) - (x2-x3)*(y1-y3)]*0.5
//
// 多边形的面积公式：
//
// |x1 y1| |x2 y2| ... |xn yn| = 0.5*abs(x1*y2-y1*x2+x2*y3-y2*x3+...+xn*y1-yn*x1)