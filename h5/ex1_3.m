X = [-9 11 -21 63 -252; 
     70 -69 141 -421 1684; 
     -575 575 -1149 3451 -13801; 
     3891 -3891 7782 -23345 93365; 
     1024 -1024 2048 -6144 24572];

num = 10000;

eig_X = eig(X);
svd_X = svd(X);

e1 = zeros(size(eig_X));
e2 = zeros(size(svd_X));

for i = 1:num
    delta = eps * 1e4 * randn(size(X));

    perturbed_eig = eig(X + delta);
    perturbed_svd = svd(X + delta);

    e1 = e1 + perturbed_eig - eig_X;
    e2 = e2 + perturbed_svd - svd_X;
end

fprintf('Accumulated Error of Eigenvalues over %d tests:\n', num);
for i = 1:length(e1)
    fprintf('Eigenvalue %d: Error = %g + %gi\n', i, real(e1(i)), imag(e1(i)));
end

fprintf('\nAccumulated Error of Singular Values over %d tests:\n', num);
for i = 1:length(e2)
    fprintf('Singular Value %d: Error = %g\n', i, e2(i));
end


