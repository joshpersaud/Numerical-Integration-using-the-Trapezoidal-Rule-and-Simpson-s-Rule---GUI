function I = SimpsonRule(f, x)
n = size(x, 2);
h = (x(n) - x(1))/(n-1);

I = (h/3) * (f(x(1)) + (2*sum(f(x(3:2:end-1)))) + (4*sum(f(x(2:2:end-1)))));
end
