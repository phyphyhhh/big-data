% Generate 100 random matrices
num = 100;
size = [1000, 100];

svd_time = 0;
svd_transpose_time = 0;
eig_time = 0;
eig_transpose_time = 0;

for i = 1:num
    X = rand(size);
    
    % Measure SVD of X
    tic;
    svd(X);
    svd_time = svd_time + toc;
    
    % Measure SVD of X transpose
    tic;
    svd(X');
    svd_transpose_time = svd_transpose_time + toc;
    
    % Measure eigenvalues of XX'
    tic;
    eig(X * X');
    eig_time = eig_time + toc;
    
    % Measure eigenvalues of X'X
    tic;
    eig(X' * X);
    eig_transpose_time = eig_transpose_time + toc;
end

% Output total times
total_svd_time = svd_time
total_svd_transpose_time = svd_transpose_time
total_eig_time = eig_time
total_eig_transpose_time = eig_transpose_time

