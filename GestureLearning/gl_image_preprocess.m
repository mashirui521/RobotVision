function stFeature = gl_image_preprocess(asFilePath, sFeatureName)
% FUNCTION stFeature = gl_image_preprocess(asFilePath, sFeatureName)
% Preprocess pattern images for maschine learning
% 
% --- INPUT
% asFilePath           array      path to the pattern image files
% sFeatureName         char       name of the feature
%
% --- OUTPUT
% stFeature            structure
%   .sName             char       name of the feature
%   .aData             array      the processed feature data
% 

aFeatureData = cell(length(asFilePath), 1);

for i = 1:length(asFilePath)
    
    sFilePath = asFilePath{i};
    
    %% segment gesture from the image
    bwDilImage = i_segmentPicture(sFilePath);
    
    %% cut the picture to square
    aCrushedPicture = i_cutPicture(bwDilImage);
    if i > 1
        aFeatureData{end + 1} = aCrushedPicture;
    else
        aFeatureData{end} = aCrushedPicture;
    end
end

stFeature = stuct('sName', sFeatureName, ...
                  'aData', aFeatureData);
return;
end

%% internal functions
% segment picture
function bwDilImage = i_segmentPicture(sFilePath)

imageIntentsity = rgb2gray(imread(sFilePath));
imageIntentsity = imfilter(imageIntentsity, fspecial('average', [4, 3]));

bwImage = edge(imageIntentsity, 'sobel');

se90 = strel('line', 3, 150);
se0 = strel('line', 3, 0);
bwDilImage = imdilate(bwImage, [se90, se0]);

return;
end

% cut picture
function aCrushedPicture = i_cutPicture(picture)
[nRow, nCol] = size(picture);
nResize = (nCol - nRow)/2;
picture = picture(:, (1 + nResize):(nCol-nResize));

aCrushedPicture = i_crush(picture);
end

% crush picture to pieces with 3*3 pixel
% and insert the bias
function aCrushedPicture = i_crush(picture)
bias = ones(3, 1);
nSize = length(picture);
aCrushedPicture = i_initBuffer(nSize);

for i = 1:(nSize/3-1)
    for j = 1:(nSize/3-1)
        itr_r = (3*(i-1) + 1): (3*(i-1) + 3);
        itr_c = (3*(j-1) + 1): (3*(j-1) + 3);
        picturePiece = [bias, picture(itr_r, itr_c)];
        aCrushedPicture(i, j) = {picturePiece};
    end
end

end

% initilize buffer to save picture pieces
function aCrushedPicture = i_initBuffer(nSize)

nBufferSize = nSize/3;
aCrushedPicture = cell(nBufferSize, nBufferSize);

for i = 1:nBufferSize
    for j = 1:nBufferSize
        aCrushedPicture(i, j) = {zeros(3, 4)};
    end
end

return;
end
