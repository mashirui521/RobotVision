function picture = mls_supporter_receivePicture(hSupporter, sOption)
% FUNCTION picture = mls_supporter_receivePicture(hService, sOption)
% Receive picture from client
% 
% --- INPUT
% hSupporter           JavaObject  The supporter instance
% sOption              char        options
%                                  '-gray'   return grayscaled picture
%                                  '-rgb'    return RGB picture(DEFAULT)
% 
% --- OUTPUT
% stInfo            
%   .hSupporter        JavaObject  The supporter instance
%   .sClientIPAddress  char        The ip address of client
%   .nPort             int         The port of client
% 

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

%% check supporter
if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.ServerSupporter')
    error('invalid supporter');
end

%% receive picture data and convert data to rgb
hSupporter.receivePicture();
hSupporter.adaptByteToRGB();

%% get R, G, B submatrix
dataR = hSupporter.getR();
dataG = hSupporter.getG();
dataB = hSupporter.getB();

R = zeros(480, 640);
G = zeros(480, 640);
B = zeros(480, 640);
for i = 1:480
    R(i, :) = dataR((640 * (i - 1) + 1):(640*i));
    G(i, :) = dataG((640 * (i - 1) + 1):(640*i));
    B(i, :) = dataB((640 * (i - 1) + 1):(640*i));
end

%% make RGB picture
picture(:, :, 1) = R./255;
picture(:, :, 2) = G./255;
picture(:, :, 3) = B./255;

%% convert RGB to gray
if ~nRGB
    picture = rgb2gray(picture);
end

return;
end