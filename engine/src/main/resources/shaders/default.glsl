#type vertex
#version 460 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec4 aColor;
layout(location = 2) in float aTexIndex;
layout(location = 3) in vec2 aTexCoords;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out float fTexIndex;
out vec2 fTexCoords;

void main(){
    fColor = aColor;
	fTexIndex = aTexIndex;
	fTexCoords = aTexCoords;

    gl_Position = uProjection*uView*vec4(aPos, 0, 1);
}

// -----------------------------------------------------------------------------
#type fragment
#version 460 core

in vec4 fColor;
in float fTexIndex;
in vec2 fTexCoords;

uniform sampler2D uTextures[8];

out vec4 color;

void main(){
	color = fTexIndex >= 0 && fTexIndex < 8 ?
		fColor*texture(uTextures[int(fTexIndex)], fTexCoords) : fColor;
}