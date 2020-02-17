function answer = LagarianInterpolation(x, f, z)
num_points = size(x, 2); 
inter_value = 0;
for i = 1: num_points 
    lagrangian = 1;
    for j = 1: num_points 
        if j ~= i
            lagrangian = lagrangian*((z - x(j)) / (x(i) - x(j)));
        end
    end
    inter_value = (lagrangian * f(i)) + inter_value;
end

answer = inter_value;

end