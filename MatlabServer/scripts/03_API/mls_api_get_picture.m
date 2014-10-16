function mPicture = mls_api_get_picture(hSupporter, stOption)
% FUNCTION aPicture = mls_api_get_picture(hSupporter, stOption)
% Get picture from client
% 
% --- INPUT
% hSupporter           JavaObject The supporter instance
% stOption             structure  option structure
%   .sFormat           char       picture format
%                                 '-rgb'      RGB
%                                 '-gray'     Gray
%   .bShow(optional)   boolean    show picture after read in
%                                 DEFAULT: true
%   .hParent(optional) handle     the parent for showing picture
%                                 if bShow is true
%                                 DEFAULT: new figure
%
% --- OUTPUT
% mPicture             matrix  The picture data matrix
% 

%% check input args
if nargin < 2
    error('empty argument');
end


if ~isequal(class(hSupporter), ...
    'com.robotvision.javaserver.ServerSupporter')
    error('invalid supporter');
end

if ~isfield(stOption, 'sFormat')
    error('sFormat is required in option.');
end

bShow = false;
if isfield(stOption, 'bShow')
    bShow = true;
end

hParent = figure;
if bShow
    if isfield(stOption, 'hParent')
        hParent = stOption.hParent;
    end
end

%% get picture from client
mPicture = mls_supporter_receivePicture(hSupporter, stOption.sFormat);

%% show picture
if bShow
    imshow(mPicture, 'Parent', hParent);
end

return;
end