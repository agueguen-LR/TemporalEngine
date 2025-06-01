package games.temporalstudio.temporalengine.rendering.component;

import games.temporalstudio.temporalengine.component.Component;

public sealed interface Render extends Component
	permits ColorRender, TextureRender
{
	
}
