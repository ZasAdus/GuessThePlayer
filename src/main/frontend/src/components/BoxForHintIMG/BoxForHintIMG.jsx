import './BoxForHintIMG.css'
import '../../styles/boxForHint.css'

function BoxForHintIMG({ src, alt, category }) {
    if (!src) return null;

    return (
        <>
            <div className="boxForHint">
                <img src={src} alt={alt || ''} className="image" />
            </div>
        </>
    );
}
export default BoxForHintIMG;