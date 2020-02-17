function answer=LagrangeInterpolation(X, Y, x)
n = size(X,2);
sum = 0;
for i = 1:n
    L=Y(i);
    for j = 1:n
        if (i~=j)
            L = L * (x - X(j))/(X(i)-X(j)); 
        end
    end
    sum = sum + L;
end
answer = sum;
end