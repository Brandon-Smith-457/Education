%Part a)
size = 250;
numRectangles = 98;
im = zeros(250);
for i = 1 : numRectangles
    randomX1 = randi([1 250]);
    randomX2 = randi([1 250]);
    randomY1 = randi([1 250]);
    randomY2 = randi([1 250]);
    randomIntensity = randi([0 255]);
    
    if (randomX2 - randomX1) < 0
        temp = randomX2;
        randomX2 = randomX1;
        randomX1 = temp;
    end
    if (randomY2 - randomY1) < 0
        temp = randomY2;
        randomY2 = randomY1;
        randomY1 = temp;
    end
    
    for j = randomX1 : randomX2
        for k = randomY1 : randomY2
            im(j, k) = randomIntensity;
        end
    end
end
im = uint8(im);
figure, imshow(im)


%Part b)
width = 20;
sigma = 3;
gaussianFilter = fspecial('gaussian', width, sigma);
laplacianFilter = fspecial('laplacian');
laplacianOfGaussianFilter = fspecial('log', width, sigma);
laplacianOfGaussianFilterConv = conv2(gaussianFilter, laplacianFilter);
laplacianOfGaussianFilterNorm = normalize(laplacianOfGaussianFilter);
laplacianOfGaussianFilterRescale = rescale(laplacianOfGaussianFilter);

figure, imshow(imresize(laplacianOfGaussianFilter, 50, "nearest"))
colorbar()

figure, imshow(imresize(laplacianOfGaussianFilterNorm, 50, "nearest"))
colorbar()

figure, imshow(imresize(laplacianOfGaussianFilterRescale, 50, "nearest"))
colorbar()

%Part c)
filteredIm = conv2(im, laplacianOfGaussianFilter);
figure, imshow(filteredIm)

filteredNormIm = conv2(im, laplacianOfGaussianFilterNorm);
figure, imshow(filteredIm)

filteredRescaleIm = conv2(im, laplacianOfGaussianFilterRescale);
figure, imshow(filteredIm)

xShiftVector = ones([1 size]);
yShiftVector = ones([size 1]);
xShiftedIm = circshift(filteredIm, xShiftVector);
yShiftedIm = circshift(filteredIm, yShiftVector);

yEdgeIm = filteredIm - yShiftedIm;
xEdgeIm = filteredIm - xShiftedIm;

yEdgeBinIm = yEdgeIm < 0;
xEdgeBinIm = xEdgeIm < 0;

figure, imshow(yEdgeBinIm)
figure, imshow(xEdgeBinIm)



