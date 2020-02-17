function answer = TrapezoidalRule(f, X)
    n=size(X,2);
    a = X(1);
    b = X(n);
    h = (b-a)/(n-1);
    fa = f(a);
    fb = f(b);
    sum=0;
    for i = 2:n-1
        x=X(i);
        sum = sum + f(x);
    end
    answer = (h/2)*( fa + 2*sum + fb );
end