function picture = mls_supporter_receivePicture(hService, sOption)

if nargin < 1
    error('empty argument');
elseif nargin < 2
    nRGB = true;
else
    switch sOption
        case '-gray'
            nRGB = false;
        case '-rgb'
            nRGB = true;
        otherwise
            error('invalid option "%s"', sOption);
    end
end

hService.receivePicture();
hService.adaptByteToRGB();

dataR = hService.getR();
dataG = hService.getG();
dataB = hService.getB();

R = zeros(480, 640);
G = zeros(480, 640);
B = zeros(480, 640);
for i = 1:480
    R(i, :) = dataR((640 * (i - 1) + 1):(640*i));
    G(i, :) = dataG((640 * (i - 1) + 1):(640*i));
    B(i, :) = dataB((640 * (i - 1) + 1):(640*i));
end

picture(:, :, 1) = R./255;
picture(:, :, 2) = G./255;
picture(:, :, 3) = B./255;

if ~nRGB
    picture = rgb2gray(picture);
end

return;
end