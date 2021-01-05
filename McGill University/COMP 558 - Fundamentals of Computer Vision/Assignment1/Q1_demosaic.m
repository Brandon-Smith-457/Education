%Part a)
puppy = imread("Puppy.jpg");
figure, imshow(puppy)

[rows, cols, numColourChannels] = size(puppy);
outputIm = puppy;

for col = 1 : cols
    for row = 1 : rows
        if ((mod(col, 2) == 1 && mod(row, 2) == 1) || (mod(col, 2) == 0 && mod(row, 2) == 0))
            channels = [1 3];
        elseif (mod(col, 2) == 0 && mod(row, 2) == 1)
            channels = [1 2];
        else
            channels = [2 3];
        end
        outputIm(row, col, channels) = 0;
    end
end

figure, imshow(outputIm)
figure, imshow(imresize(imcrop(outputIm, [1 1 20 20]), 50, "nearest"))

redChannel = outputIm(:,:,1);
greenChannel = outputIm(:,:,2);
blueChannel = outputIm(:,:,3);
figure, imshow(imresize(imcrop(redChannel, [1 1 20 20]), 50, "nearest"))
figure, imshow(imresize(imcrop(greenChannel, [1 1 20 20]), 50, "nearest"))
figure, imshow(imresize(imcrop(blueChannel, [1 1 20 20]), 50, "nearest"))


%Part b)
%flattenedIm = redChannel + greenChannel + blueChannel;
%figure, imshow(flattenedIm)
%figure, imshow(demosaic(flattenedIm, "gbrg"))
greenFilter = [0 0.25 0; 0.25 1 0.25; 0 0.25 0];
greenConv = conv2(greenChannel, greenFilter);
greenConv = uint8(greenConv);
figure, imshow(greenConv)

redBlueFilter = [0.25 0.5 0.25; 0.5 1 0.5; 0.25 0.5 0.25];
redConv = conv2(redChannel, redBlueFilter);
redConv = uint8(redConv);
figure, imshow(redConv)

blueConv = conv2(blueChannel, redBlueFilter);
blueConv = uint8(blueConv);
figure, imshow(blueConv)

finalDemosaicedIm = cat(3, redConv, greenConv, blueConv);
figure, imshow(finalDemosaicedIm)


%Part c) The convolutional approach to image reconstructing tends to create
%a "blur" along intensity shifts, which is made very noticeable with
%synthetically produced intensity edges
synthRGBIm = imread("HighIntensityEdge.png");
figure, imshow(imresize(imcrop(synthRGBIm, [1 1 20 20]), 50, "nearest"))

[rows, cols, numColourChannels] = size(synthRGBIm);
outputIm = synthRGBIm;

for col = 1 : cols
    for row = 1 : rows
        if ((mod(col, 2) == 1 && mod(row, 2) == 1) || (mod(col, 2) == 0 && mod(row, 2) == 0))
            channels = [1 3];
        elseif (mod(col, 2) == 0 && mod(row, 2) == 1)
            channels = [1 2];
        else
            channels = [2 3];
        end
        outputIm(row, col, channels) = 0;
    end
end

redChannel = outputIm(:,:,1);
greenChannel = outputIm(:,:,2);
blueChannel = outputIm(:,:,3);


%Part b)
greenFilter = [0 0.25 0; 0.25 1 0.25; 0 0.25 0];
redBlueFilter = [0.25 0.5 0.25; 0.5 1 0.5; 0.25 0.5 0.25];

redConv = conv2(redChannel, redBlueFilter);
redConv = uint8(redConv);

greenConv = conv2(greenChannel, greenFilter);
greenConv = uint8(greenConv);

blueConv = conv2(blueChannel, redBlueFilter);
blueConv = uint8(blueConv);

finalDemosaicedIm = cat(3, redConv, greenConv, blueConv);
figure, imshow(imresize(imcrop(finalDemosaicedIm, [1 1 20 20]), 50, "nearest"))



